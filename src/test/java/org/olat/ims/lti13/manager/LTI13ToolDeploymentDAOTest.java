/**
 * <a href="http://www.openolat.org">
 * OpenOLAT - Online Learning and Training</a><br>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); <br>
 * you may not use this file except in compliance with the License.<br>
 * You may obtain a copy of the License at the
 * <a href="http://www.apache.org/licenses/LICENSE-2.0">Apache homepage</a>
 * <p>
 * Unless required by applicable law or agreed to in writing,<br>
 * software distributed under the License is distributed on an "AS IS" BASIS, <br>
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. <br>
 * See the License for the specific language governing permissions and <br>
 * limitations under the License.
 * <p>
 * Initial code contributed and copyrighted by<br>
 * frentix GmbH, http://www.frentix.com
 * <p>
 */
package org.olat.ims.lti13.manager;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.olat.core.commons.persistence.DB;
import org.olat.core.id.Identity;
import org.olat.ims.lti13.LTI13Tool;
import org.olat.ims.lti13.LTI13ToolDeployment;
import org.olat.ims.lti13.LTI13ToolType;
import org.olat.repository.RepositoryEntry;
import org.olat.test.JunitTestHelper;
import org.olat.test.OlatTestCase;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * Initial date: 22 févr. 2021<br>
 * @author srosse, stephane.rosse@frentix.com, http://www.frentix.com
 *
 */
public class LTI13ToolDeploymentDAOTest extends OlatTestCase {
	
	@Autowired
	private DB dbInstance;
	@Autowired
	private LTI13ToolDAO lti13ToolDao;
	@Autowired
	private LTI13ToolDeploymentDAO lti13ToolDeploymentDao;
	
	@Test
	public void createDeployment() {
		String toolName = "LTI 1.3 spring demo";
		String toolUrl = "https://www.openolat.com/tool";
		String clientId = UUID.randomUUID().toString();
		String initiateLoginUrl = "https://www.openolat.com/lti/api/login";
		LTI13Tool tool = lti13ToolDao.createTool(toolName, toolUrl, clientId, initiateLoginUrl, LTI13ToolType.EXTERNAL);
		
		Identity author = JunitTestHelper.createAndPersistIdentityAsRndAuthor("lti-13-author-1");
		RepositoryEntry entry = JunitTestHelper.deployBasicCourse(author);
		
		LTI13ToolDeployment deployment = lti13ToolDeploymentDao.createDeployment(null, tool, entry, "283647");
		dbInstance.commitAndCloseSession();
		
		Assert.assertNotNull(deployment);
		Assert.assertNotNull(deployment.getKey());
		Assert.assertNotNull(deployment.getDeploymentId());
		Assert.assertEquals(tool, deployment.getTool());
		Assert.assertEquals(entry, deployment.getEntry());
		Assert.assertEquals("283647", deployment.getSubIdent());
	}

}
