package com.onlyoffice.integration.documentserver.models.filemodel;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Template implements Serializable{  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
// the document template parameters
    private String image;  // the absolute URL to the image for template
    private String title;  // the template title that will be displayed in the <b>Create New...</b> menu option
    private String url;  // the absolute URL to the document where it will be created and available after creation
}
