package com.sample.hudson;

import hudson.Launcher;
import hudson.Extension;
import hudson.util.FormValidation;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.AbstractProject;
import hudson.tasks.Builder;
import hudson.tasks.BuildStepDescriptor;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import javax.servlet.ServletException;
import java.io.IOException;
import hudson.EnvVars;

//import com.cts.*;
//import com.cts.UpdateXmlFile;
import com.cts.BART.*;



public class BART extends Builder{
	private  String task;
   	private  String projectName;
	private  String User;
	

	@DataBoundConstructor
	public BART(String task,String projectName,String User) {
        this.task = task;
        this.projectName=projectName;
        this.User=User;
 
		
	}

        
    public String getTask() {
		return task;
	}
	
    public String getprojectName() {
		return projectName;
	}
	 
   public String getUser() {
		return User;
	}
   
	
   public void Process_Bart(String task, String projectName,
			String User, BuildListener listener) {
	   try {
		Bart_Integration.BuildProcess_RunBART(task, projectName, User);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}

	/*
	@Override
	public boolean perform(AbstractBuild build, Launcher launcher,
			BuildListener listener) 
	{
		System.out.println("...........BART Report Generation........");
		try
		{
		Process_Bart(task, projectName, User,listener);
		return true;
		}
		catch (Exception e) 
		{
			System.err.println("ERROR OCCURRED!!! \n"+ e.getMessage());
	        e.printStackTrace();
	        return false;
	        //System.exit(42);
		}
		
		
		
	} */
	                @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher,BuildListener listener)throws InterruptedException,IOException
    {

        EnvVars environment = build.getEnvironment(listener);
        String expandedProjectName = environment.expand(projectName);
        String expandedUser = environment.expand(User);
        System.out.println("...........BART Report Generation........");
        try {
            Process_Bart (task, expandedProjectName, expandedUser,listener);
        } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
        }
                                
                                

        return true;
    }
	


	@Override
	public DescriptorImpl getDescriptor() {
		return (DescriptorImpl) super.getDescriptor();
	}

	/*
	 * Descriptor for {@link EarBuilder}. Used as a singleton. The class is
	 * marked as public so that it can be accessed from views.
	 * 
	 * <p>
	 * See <tt>views/hudson/plugins/hello_world/EarBuilder/*.jelly</tt> for the
	 * actual HTML fragment for the configuration screen.
	 */
	@Extension
	// this marker indicates Hudson that this is an implementation of an
	// extension point.
	public static final class DescriptorImpl extends
			BuildStepDescriptor<Builder> {
		/**
		 * To persist global configuration information, simply store it in a
		 * field and call save().
		 * 
		 * <p>
		 * If you don't want fields to be persisted, use <tt>transient</tt>.
		 */

		/**
		 * Performs on-the-fly validation of the form field 'name'.
		 * 
		 * @param value
		 *            This parameter receives the value that the user has typed.
		 * @return Indicates the outcome of the validation. This is sent to the
		 *         browser.
		 */
		public FormValidation doCheckName(@QueryParameter String value)
				throws IOException, ServletException {
			if (value.length() == 0)
				return FormValidation.error("Please set a name");

			return FormValidation.ok();
		}

		public boolean isApplicable(Class<? extends AbstractProject> aClass) {
			// indicates that this builder can be used with all kinds of project
			// types
			return true;
		}

		/**
		 * This human readable name is used in the configuration screen.
		 */
		public String getDisplayName() {
			return "cognizant.bpi.tibco.bw5.BART";
		}

		@Override
		public boolean configure(StaplerRequest req, JSONObject formData)
				throws FormException {
			// To persist global configuration information,
			// set that to properties and call save().
			// useFrench = formData.getBoolean("useFrench");
			// ^Can also use req.bindJSON(this, formData);
			// (easier when there are many fields; need set* methods for this,
			// like setUseFrench)
			save();
			return super.configure(req, formData);
		}

		/**
		 * This method returns true if the global configuration says we should
		 * speak French.
		 */

	}
	
	
}
