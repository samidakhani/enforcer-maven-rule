# enforcer-maven-rule
   A customized rule for maven-enforcer-plugin to check if the machine is connected to network.
	
# Usage
   		   <plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-enforcer-plugin</artifactId>
				<version>1.4.1</version>
				<executions>
					<execution>
						<id>enforce-versions</id>
						<phase>validate</phase>
						<goals>
							<goal>enforce</goal>
						</goals>
						<configuration>
							<rules>
								<networkConnectorRule
									implementation="org.dakhani.learn.enforcer.rule.NetworkConnectorRule">
									<checkNetwork>true</checkNetwork>
								</networkConnectorRule>
							</rules>
						</configuration>
					</execution>
				</executions>
				<dependencies>
					<dependency>
						<groupId>org.dakhani.learn</groupId>
						<artifactId>enforcer-maven-rule</artifactId>
						<version>0.0.1</version>
					</dependency>
				</dependencies>
			</plugin>
	
# Motivation
  Often our code relies on network-connectivity to perform business-logic; for-example: to utilize third party APIs.
  This rule is beneficial in such cases, where you need to validate that your machine is connected to the network.
