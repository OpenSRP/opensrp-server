import org.junit.Test;
import org.opensrp.formdownload.FormDownLoadController;


public class FormDownloadTest 
{
	

	@Test
	public void shouldReturnAvailableVersion()
	{
		FormDownLoadController formDownLoadController = new FormDownLoadController("/home/wahid/form_name_1/", "form_definition.json");
		
		String versions = formDownLoadController.getAllAvailableVersion();
		System.out.println("available version:::"+versions);
		//formDownLoadController.getForm("PNC_Registration_EngKan");
	}	
}
