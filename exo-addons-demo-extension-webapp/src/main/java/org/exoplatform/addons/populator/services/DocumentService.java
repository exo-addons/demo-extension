package org.exoplatform.addons.populator.services;

import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.ext.app.SessionProviderService;
import org.exoplatform.services.jcr.ext.common.SessionProvider;
import org.exoplatform.services.jcr.ext.hierarchy.NodeHierarchyCreator;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jcr.Node;
import javax.jcr.Session;
import java.io.InputStream;
import java.util.Calendar;
import java.util.logging.Logger;

@Named("documentService")
@ApplicationScoped
public class DocumentService {

  Logger log = Logger.getLogger("DocumentService");

  RepositoryService repositoryService_;
  SessionProviderService sessionProviderService_;
  NodeHierarchyCreator nodeHierarchyCreator_;

  @Inject
  public DocumentService(RepositoryService repositoryService, SessionProviderService sessionProviderService, NodeHierarchyCreator nodeHierarchyCreator)
  {
    repositoryService_ = repositoryService;
    sessionProviderService_ = sessionProviderService;
    nodeHierarchyCreator_= nodeHierarchyCreator;
  }

  public void uploadDocuments()
  {
    storeFile("dici_elyseo_dynamique_en.pdf", "human_resources", false, null);
  }

  protected void storeFile(String filename, String name, boolean isPrivateContext, String uuid)
  {
    SessionProvider sessionProvider = getUserSessionProvider();
    InputStream inputStream = Utils.getDocument(filename);

    try
    {
      //get info
      Session session = sessionProvider.getSession("collaboration", repositoryService_.getCurrentRepository());

      Node homeNode;

      if (isPrivateContext)
      {
        Node userNode = nodeHierarchyCreator_.getUserNode(sessionProvider, name);
        homeNode = userNode.getNode("Private");
      }
      else
      {
        Node rootNode = session.getRootNode();
        homeNode = rootNode.getNode(getSpacePath(name));
      }

      Node docNode = homeNode.getNode("Documents");


      if (!docNode.hasNode(filename) && (uuid==null || "---".equals(uuid)))
      {
        Node fileNode = docNode.addNode(filename, "nt:file");
        Node jcrContent = fileNode.addNode("jcr:content", "nt:resource");
        jcrContent.setProperty("jcr:data", inputStream);
        jcrContent.setProperty("jcr:lastModified", Calendar.getInstance());
        jcrContent.setProperty("jcr:encoding", "UTF-8");
        if (filename.endsWith(".jpg"))
          jcrContent.setProperty("jcr:mimeType", "image/jpeg");
        else if (filename.endsWith(".png"))
          jcrContent.setProperty("jcr:mimeType", "image/png");
        else if (filename.endsWith(".pdf"))
          jcrContent.setProperty("jcr:mimeType", "application/pdf");
        else if (filename.endsWith(".doc"))
          jcrContent.setProperty("jcr:mimeType", "application/vnd.ms-word");
        else if (filename.endsWith(".xls"))
          jcrContent.setProperty("jcr:mimeType", "application/vnd.ms-excel");
        else if (filename.endsWith(".ppt"))
          jcrContent.setProperty("jcr:mimeType", "application/vnd.ms-powerpoint");
        else if (filename.endsWith(".docx"))
          jcrContent.setProperty("jcr:mimeType", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        else if (filename.endsWith(".xlsx"))
          jcrContent.setProperty("jcr:mimeType", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        else if (filename.endsWith(".pptx"))
          jcrContent.setProperty("jcr:mimeType", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
        else if (filename.endsWith(".odp"))
          jcrContent.setProperty("jcr:mimeType", "application/vnd.oasis.opendocument.presentation");
        else if (filename.endsWith(".odt"))
          jcrContent.setProperty("jcr:mimeType", "application/vnd.oasis.opendocument.text");
        else if (filename.endsWith(".ods"))
          jcrContent.setProperty("jcr:mimeType", "application/vnd.oasis.opendocument.spreadsheet");
        session.save();
      }
      else
      {
        Node fileNode=null;
        if (uuid!=null) {
          fileNode = session.getNodeByUUID(uuid);
        }
        else
        {
          fileNode = docNode.getNode(filename);
        }
        if (fileNode.canAddMixin("mix:versionable")) fileNode.addMixin("mix:versionable");
        if (!fileNode.isCheckedOut()) {
          fileNode.checkout();
        }
        fileNode.save();
        fileNode.checkin();
        fileNode.checkout();
        Node jcrContent = fileNode.getNode("jcr:content");
        jcrContent.setProperty("jcr:data", inputStream);
        session.save();
      }

    }
    catch (Exception e)
    {
      System.out.println("JCR::" + e.getMessage());
    }
  }

  private static String getSpacePath(String space)
  {
    return "Groups/spaces/"+space;
  }

  public SessionProvider getUserSessionProvider() {
    SessionProvider sessionProvider = sessionProviderService_.getSessionProvider(null);
    return sessionProvider;
  }

  public SessionProvider getSystemSessionProvider() {
    SessionProvider sessionProvider = sessionProviderService_.getSystemSessionProvider(null);
    return sessionProvider;
  }

}
