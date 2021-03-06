package org.jenkins.plugins;


import org.jenkinsci.plugins.vmanager.JUnitRequestHolder;
import org.jenkinsci.plugins.vmanager.Utils;
import org.jenkinsci.plugins.vmanager.StepHolder;

public class TestPlugin {

	
	final String vAPIUrl = "https://vlnx489:50500/vmgr/vapi";
    final boolean authRequired = true;
    final String vAPIUser = "tyanai";
    final String vAPIPassword = "letmein";
    final String vSIFName = "/home/tyanai/vsif/vm_basic.vsif";
    final String vSIFInputFile  = "d:/temp/artifacts/vsifs.input";
    final boolean deleteInputFile = false;
    final String vsifType = "dynamic";
    
    final int buildNumber = 83;
    final String buildID = "2014-45-45-34-56-78";
    final String buildArtifactPath = "d:/temp/artifacts";
    
    private String inaccessibleResolver = "fail";
	private String stoppedResolver = "fail";
	private String failedResolver = "fail";
	private String doneResolver = "ignore";
	private String suspendedResolver = "fail";
	private boolean waitTillSessionEnds = true;
	private int stepSessionTimeout = 10;
	
	private final boolean generateJUnitXML = true;
	private final boolean extraAttributesForFailures = true; 
	private final String staticAttributeList = "top_files,test_group,verification_scope";
	private final boolean markBuildAsFailedIfAllRunFailed = false;
        private final boolean markJobAsFailedIfAllRunFailed = false;
        private final boolean markBuildAsPassedIfAllRunPassed = false;
        private final boolean failJobUnlessAllRunPassed = false;
	
    	
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		TestPlugin testPlugin = new TestPlugin();
		testPlugin.test();
		

	}
	
	public void test() throws Exception{
		
		Utils utils = new Utils();
		
		//Test Reading VSIF input file
		String[] vsifFileNames = null;
		if ("static".equals(vsifType)) {
			vsifFileNames = new String[1];
			vsifFileNames[0] = vSIFName;
		} else {		
			vsifFileNames = utils.loadVSIFFileNames(buildID, buildNumber, buildArtifactPath, vSIFInputFile, null,deleteInputFile);
		}
		
		
		//Test connection testing
		utils.checkVAPIConnection(vAPIUrl, authRequired, vAPIUser, vAPIPassword);
		
		//Test attributes for JUnit taken from runs
		String testAttr = utils.checkExtraStaticAttr(vAPIUrl, authRequired, vAPIUser, vAPIPassword,"comment,status,first_failure_name");
		System.out.println(testAttr);
		
		
		StepHolder stepHolder = null;
		if (waitTillSessionEnds){
			JUnitRequestHolder jUnitRequestHolder = new JUnitRequestHolder(generateJUnitXML, extraAttributesForFailures, staticAttributeList,false);
			stepHolder = new StepHolder(inaccessibleResolver, stoppedResolver, failedResolver, doneResolver, suspendedResolver, waitTillSessionEnds,stepSessionTimeout,jUnitRequestHolder,markBuildAsFailedIfAllRunFailed,markJobAsFailedIfAllRunFailed,markBuildAsPassedIfAllRunPassed,failJobUnlessAllRunPassed);
		}
		utils.executeVSIFLaunch(vsifFileNames, vAPIUrl, authRequired, vAPIUser, vAPIPassword, null,false,buildID,buildNumber,buildArtifactPath,0,0,false,null,false,null,null, stepHolder,"",buildArtifactPath,null,false);
		
		//String buildStatus = BuildStatusMap.getValue(buildID, buildNumber, buildArtifactPath+"", "id", true);
		//System.out.println("Build status is '" + buildStatus + "'" );
		
		
		
		//utils.executeAPI("{}", "/sessions/count", vAPIUrl, authRequired, vAPIUser, vAPIPassword, "POST", null, false, buildID+"-1", buildNumber, buildArtifactPath,0,0,false);
		//utils.executeAPI("{}", "/runs/get?id=5", vAPIUrl, authRequired, vAPIUser, vAPIPassword, "GET", null, false, buildID+"-2", buildNumber, buildArtifactPath,0,0,false);
	}
	
	
	

}
