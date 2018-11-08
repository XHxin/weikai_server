package cc.modules.util;

import java.io.File;

import com.baidubce.BceClientConfiguration;
import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.services.doc.DocClient;
import com.baidubce.services.doc.model.GetDocumentResponse;

public class BDocHelper {
	private static String ACCESS_KEY_ID = "dd416f9ad71e449d97d06c1b3962a68c";
	private static String SECRET_ACCESS_KEY = "d8e2e6935b704bab8d25614df97e57db";

	public static String upLoadToBCE(File file) {
		BceClientConfiguration config = new BceClientConfiguration();
		config.setCredentials(new DefaultBceCredentials(ACCESS_KEY_ID, SECRET_ACCESS_KEY));
		final DocClient client = new DocClient(config);
		return client.createDocument(file, file.getName()).getDocumentId();
	}

	public static GetDocumentResponse readDocument(String docid) {
		BceClientConfiguration config = new BceClientConfiguration();
		config.setCredentials(new DefaultBceCredentials(ACCESS_KEY_ID, SECRET_ACCESS_KEY));
		final DocClient client = new DocClient(config);
		GetDocumentResponse getDocument = client.getDocument(docid);
//		getDocument.getTitle();    		//附件名称
//		getDocument.getFormat();		//格式
//		getDocument.getDocumentId();	//documentID
//		getDocument.getPublishInfo().getPageCount();  	//页码
		return getDocument;
	}
}
