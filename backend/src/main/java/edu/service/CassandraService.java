package edu.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.exceptions.NoHostAvailableException;
import com.datastax.driver.core.exceptions.QueryExecutionException;
import com.datastax.driver.core.exceptions.QueryValidationException;

import edu.util.DbSchemaTags;

@Service
public class CassandraService {
	private static final Logger LOGGER = LoggerFactory.getLogger(CassandraService.class);

	private static final String CASSANDRA_EXCEPTION = "Cassandra exception {}";

	@Value("${cassandra.host}")
	private String cassandraHost;
	@Value("${cassandra.user}")
	private String cassandraUser;
	@Value("${cassandra.pass}")
	private String cassandraPass;
	@Value("${cassandra.auth.required}")
	private String cassandraAuthRequired;
	@Value("${cassandra.port.native}")
	private int cassandraPort;
	@Value("classpath:dbSchema.xml")
	private Resource dbSchemaResource;

	@Value("classpath:patches")
	private Resource patches;

	private Session session;
	private Document document;

	// @Autowired
	// private HistoryDaoCassandra historyDaoCassandra;

	@PostConstruct
	public void connectDataBase() {
		System.out.println(cassandraHost);
		System.out.println(cassandraPort);
		System.out.println(cassandraUser);
		try {
			Builder clusterBuilder = Cluster.builder().addContactPoint(cassandraHost).withPort(cassandraPort);
			if (Boolean.valueOf(cassandraAuthRequired)) {
				clusterBuilder.withCredentials(cassandraUser, cassandraPass);
			}
			Cluster cluster = clusterBuilder.build();
			//Configuration configuration = cluster.getConfiguration();
			session = cluster.connect();
			initDatabase();
		} catch (Exception e) {
			LOGGER.error("Could not connect to the database cluster {}", e.getMessage());
		}

		try {
			updateDatabase();
		} catch (Exception e) {
			LOGGER.error("Could not run update scripts {}", e.getMessage());
		}
	}

	@PreDestroy
	public void disconnectDatabase() {
		try {
			session.close();
		} catch (Exception e) {
			LOGGER.error("Could not close database exception {}", e.getMessage());
		}
	}

	public boolean checkConnection() {
		return !session.isClosed();
	}

	private void initDatabase() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.parse(dbSchemaResource.getFile());
		} catch (ParserConfigurationException | SAXException | IOException e) {
			LOGGER.error("Could not initialize database", e);
			return;
		}

		DbSchemaTags[] xmlTagsToBeProcessed = DbSchemaTags.values();
		for (DbSchemaTags tag : xmlTagsToBeProcessed) {
			NodeList statementListBasedOnTagName = document.getElementsByTagName(tag.getTagName());
			for (int i = 0; i < statementListBasedOnTagName.getLength(); i++) {
				if (!statementListBasedOnTagName.item(i).getTextContent().trim().isEmpty()) {
					String statementToBeExecuted = statementListBasedOnTagName.item(i).getTextContent()
							.replaceAll("\\s+", " ");
					try {
						execute(statementToBeExecuted);
					} catch (Exception e) {
						LOGGER.error("Could not run query " + statementToBeExecuted, e);
					}
				}
			}
		}
	}

	/**
	 * Runs all the scripts with the extension {@code cql} in the
	 * {@code patches} folder, including subfolders.
	 * <p>
	 * IMPORTANT: There is no rollback functionality, meaning that a script can
	 * be partially run in case of an error in the middle of the script.
	 */
	void updateDatabase() {
		String[] extensions = new String[] { "cql" };
		Collection<File> listFiles;

		File patchesFile;
		try {
			patchesFile = this.patches.getFile();
			listFiles = FileUtils.listFiles(patchesFile, extensions, true);
		} catch (IOException e1) {
			// LOGGER.error("Could not read patches folder " +
			// patches.getFilename(), e1);
			return;
		}

		for (File file : listFiles) {
			List<String> statements;
			try {
				statements = Files.readAllLines(file.toPath());
			} catch (IOException e) {
				LOGGER.error("Could not update database with script " + file.getAbsolutePath(), e);
				continue;
			}

			for (String statement : statements) {
				String statementToBeExecuted = statement.replaceAll("\\s+", " ");
				try {
					execute(statementToBeExecuted);
				} catch (Exception e) {
					LOGGER.error("Could not run query " + statementToBeExecuted, e);
				}
			}

		}
	}

	/**
	 * Execute the given update/insert statement (ie: no select queries).
	 * 
	 * @param statement
	 */
	public void execute(Statement statement) {
		try {
			session.execute(statement);
		} catch (NoHostAvailableException e) {
			LOGGER.error(CASSANDRA_EXCEPTION, e);
			// throw new
			// ResourceConnectionException(ErrorCode.MISSING_CONNECTION_TO_DB,
			// statement.toString());
		} catch (QueryExecutionException | QueryValidationException e) {
			LOGGER.error(CASSANDRA_EXCEPTION, e);
			// throw new InvalidDataException(ErrorCode.FAILED_QUERY_ON_DB,
			// statement.toString());
		}
	}

	/**
	 * Execute the given update/insert query (ie: no select queries).
	 * 
	 * @param query
	 */
	public void execute(String query) {
		try {
			LOGGER.debug("query:" + query);
			query = StringUtils.trimToEmpty(query);
			if (StringUtils.isNotEmpty(query) && !query.startsWith("--")) {
				session.execute(query);
			}
		} catch (NoHostAvailableException e) {
			LOGGER.error(CASSANDRA_EXCEPTION, e);
			// throw new
			// ResourceConnectionException(ErrorCode.MISSING_CONNECTION_TO_DB,
			// query);
		} catch (QueryExecutionException | QueryValidationException e) {
			// throw new InvalidDataException(ErrorCode.FAILED_QUERY_ON_DB,
			// query);
		}
	}

	public String getCurrentKeyspace() {
		return this.session.getLoggedKeyspace();
	}

	public Session getSession() {
		return this.session;
	}

	/**
	 * Execute a PreparedStatement holding a select query with the given binding
	 * values.
	 * 
	 * @param preparedStatement
	 * @param values
	 * @return result of the select query
	 */
	public ResultSet executeSelect(Statement statement) {
		try {
			return session.execute(statement);
		} catch (NoHostAvailableException e) {
			LOGGER.error(CASSANDRA_EXCEPTION, e);
			// throw new
			// ResourceConnectionException(ErrorCode.MISSING_CONNECTION_TO_DB,
			// statement.toString());
		} catch (QueryExecutionException | QueryValidationException e) {
			LOGGER.error(CASSANDRA_EXCEPTION, e);
			// throw new InvalidDataException(ErrorCode.FAILED_QUERY_ON_DB,
			// statement.toString());
		}
		return null;
	}

}
