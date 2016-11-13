package org.dakhani.learn.enforcer.rule;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.apache.maven.enforcer.rule.api.EnforcerRule;
import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.enforcer.rule.api.EnforcerRuleHelper;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.apache.maven.rtinfo.RuntimeInformation;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluationException;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.sonatype.aether.impl.ArtifactResolver;

/**
 * @author Sami Dakhani Created on Nov 13, 2016
 *
 */
public class NetworkConnectorRule implements EnforcerRule {

	private boolean checkNetwork = true;

	/**
	 * @see org.apache.maven.enforcer.rule.api.EnforcerRule#execute(org.apache.maven.enforcer.rule.api.EnforcerRuleHelper)
	 */
	@Override
	public void execute(final EnforcerRuleHelper helper)
			throws EnforcerRuleException {

		Log log = helper.getLog();

		try {

			MavenProject project = (MavenProject) helper.evaluate("${project}");
			MavenSession session = (MavenSession) helper.evaluate("${session}");

			String target = (String) helper
					.evaluate("${project.build.directory}");
			String artifactId = (String) helper
					.evaluate("${project.artifactId}");

			ArtifactResolver artifactResolver = (ArtifactResolver) helper
					.getComponent(ArtifactResolver.class);
			RuntimeInformation runtimeInfo = (RuntimeInformation) helper
					.getComponent(RuntimeInformation.class);

			log.info("Retrieved Project: " + project);
			log.info("Retrieved ArtifactId: " + artifactId);
			log.info("Retrieved Session: " + session);
			log.info("Retrieved Target Folder: " + target);

			log.info("Retrieved Resolver: " + artifactResolver);
			log.info("Retrieved RuntimeInfo: " + runtimeInfo);

			if (this.checkNetwork) {
				boolean isActive = this.isNetworkActive();

				if (isActive) {
					log.info("Connected to internet");
				} else {
					throw new EnforcerRuleException(
							"Not connected to internet");
				}
			}

		} catch (SocketException e) {
			throw new EnforcerRuleException(
					"SocketException " + e.getLocalizedMessage(), e);
		} catch (ComponentLookupException e) {
			throw new EnforcerRuleException(
					"Unable to lookup a component " + e.getLocalizedMessage(),
					e);
		} catch (ExpressionEvaluationException e) {
			throw new EnforcerRuleException(
					"Unable to lookup an expression " + e.getLocalizedMessage(),
					e);
		}

	}

	/**
	 * @see org.apache.maven.enforcer.rule.api.EnforcerRule#getCacheId()
	 */
	@Override
	public String getCacheId() {
		return Boolean.toString(this.checkNetwork);
	}

	/**
	 * @see org.apache.maven.enforcer.rule.api.EnforcerRule#isCacheable()
	 */
	@Override
	public boolean isCacheable() {
		return true;
	}

	public boolean isNetworkActive() throws SocketException {

		Enumeration<NetworkInterface> netInterfaces = NetworkInterface
				.getNetworkInterfaces();

		while (netInterfaces.hasMoreElements()) {

			NetworkInterface netInterface = netInterfaces.nextElement();
			if (netInterface.isUp() && !netInterface.isLoopback()) {
				return true;
			}
		}

		return false;

	}

	/**
	 * @see org.apache.maven.enforcer.rule.api.EnforcerRule#isResultValid(org.apache.maven.enforcer.rule.api.EnforcerRule)
	 */
	@Override
	public boolean isResultValid(final EnforcerRule cachedRule) {

		if (cachedRule.getCacheId()
				.equals(Boolean.toString(this.checkNetwork))) {
			return true;
		}

		return false;

	}

}
