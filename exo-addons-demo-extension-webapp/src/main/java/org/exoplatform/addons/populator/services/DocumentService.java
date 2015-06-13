package org.exoplatform.addons.populator.services;

import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.ext.app.SessionProviderService;
import org.exoplatform.services.jcr.ext.common.SessionProvider;
import org.exoplatform.services.jcr.ext.hierarchy.NodeHierarchyCreator;
import org.exoplatform.services.listener.ListenerService;
import org.exoplatform.services.security.ConversationState;
import org.exoplatform.services.security.Identity;
import org.exoplatform.services.security.MembershipEntry;

import juzu.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jcr.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.logging.Logger;

@Named("documentService")
@SessionScoped
public class DocumentService {

  Logger log = Logger.getLogger("DocumentService");

  RepositoryService repositoryService_;
  SessionProviderService sessionProviderService_;
  NodeHierarchyCreator nodeHierarchyCreator_;
  public static String FILE_CREATED_ACTIVITY         = "ActivityNotify.event.FileCreated";
  ListenerService listenerService_;

  @Inject
  public DocumentService(RepositoryService repositoryService, SessionProviderService sessionProviderService, NodeHierarchyCreator nodeHierarchyCreator, ListenerService listenerService)
  {
    repositoryService_ = repositoryService;
    sessionProviderService_ = sessionProviderService;
    nodeHierarchyCreator_= nodeHierarchyCreator;
    listenerService_ = listenerService;
  }

  public void uploadDocuments(String username)
  {
    storeFile("dici_elyseo_dynamique_en.pdf", "human_resources", false, null, Utils.MARY);
    storeFile("Health Guide PEI PERCU Elyseo.pdf", "human_resources", false, null, Utils.MARY);
    storeFile("eXo_overview_feb2013_V2.pdf", "social_intranet", false, null, Utils.JOHN);
    storeFile("YourOpinion-eXoPlatform35.pdf", "social_intranet", false, null, Utils.JOHN);
    storeFile("Boston Logan WiFi Home.pdf", username, true, null, username);
  }

  public void uploadDocuments2(String username)
  {
    storeFile("PUR1207_02_Quotation_File.xls", "social_intranet", false, null, Utils.JOHN);
    storeFile("Fiche_solution_Exo_Platform.pdf", "social_intranet", false, null, username);
    storeFile("May MTD 2013 Funnel report Week 21.pptx", "marketing_analytics", false, null, Utils.MARY);
    storeFile("PUR1207_02_RFP_Final.docx", "social_intranet", false, null, Utils.JOHN);
    storeVideos("Highlights on eXo Platform 4.mp4", "ask_a_colleague", false, null, Utils.JOHN, null, "collaboration", "videos");
  }

  public void uploadNews()
  {
    try {
      Session session = sessionProviderService_.getSystemSessionProvider(null).getSession("collaboration", repositoryService_.getCurrentRepository());
      if (!session.getRootNode().hasNode("sites/intranet/web contents/news"))
      {
        InputStream inputStream = Utils.getFile("news-sysview.xml", "contents");

        session.importXML("/sites/intranet/web contents",
                inputStream,
                ImportUUIDBehavior.IMPORT_UUID_CREATE_NEW);

        session.save();

      }
    } catch (IOException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    } catch (RepositoryException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
  }

  public void updateTemplates()
  {
    storeFile("OneColumn.gtmpl", "john", true, null, Utils.JOHN, "/exo:ecm/views/templates/content-list-viewer/list", "dms-system", "templates");
    storeFile("view1", "john", true, null, Utils.JOHN, "/exo:ecm/templates/exo:webContent/views", "dms-system", "templates");
    storeFile("logo.png", "root", true, null, "__system", "/Application Data/logos", "collaboration", "images");
  }

  protected void storeFile(String filename, String name, boolean isPrivateContext, String uuid, String username)
  {
    storeFile(filename, name, isPrivateContext, uuid, username, null, "collaboration", "documents");
  }
  protected void storeFile(String filename, String name, boolean isPrivateContext, String uuid, String username, String path, String workspace, String type)
  {
    SessionProvider sessionProvider = null;
    if (!"root".equals(name)) {
      sessionProvider = startSessionAs(username);
    } else {
      sessionProvider = SessionProvider.createSystemProvider();
    }

    try
    {
      //get info
      Session session = sessionProvider.getSession(workspace, repositoryService_.getCurrentRepository());

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

      if (path!=null)
      {
        Node rootNode = session.getRootNode();
        docNode = rootNode.getNode(path.substring(1));
      }

      if (!docNode.hasNode(filename) && (uuid==null || "---".equals(uuid)))
      {
        Node fileNode = docNode.addNode(filename, "nt:file");
        Node jcrContent = fileNode.addNode("jcr:content", "nt:resource");
        InputStream inputStream = Utils.getFile(filename, type);
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
        if (!"root".equals(name)) {
          listenerService_.broadcast(FILE_CREATED_ACTIVITY, null, fileNode);
        }

      } else if ("templates".equals(type)) {

        // Re Upload the file, so it will publish an activity in the Activity Stream
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
        InputStream inputStream = Utils.getFile(filename, type);
        jcrContent.setProperty("jcr:data", inputStream);
        session.save();
      }


    }
    catch (Exception e)
    {
      System.out.println("JCR::" + e.getMessage());
    }
    endSession();
  }

  protected void storeVideos(String filename, String name, boolean isPrivateContext, String uuid, String username, String path, String workspace, String type) {

    SessionProvider sessionProvider = startSessionAs(username);

    try {
      //get info
      Session session = sessionProvider.getSession(workspace, repositoryService_.getCurrentRepository());

      Node homeNode;

      Node rootNode = session.getRootNode();

      homeNode = rootNode.getNode(getSpacePath(name));

      Node docNode = homeNode.getNode("Documents");

      if (!docNode.hasNode(filename) && (uuid==null || "---".equals(uuid))) {
        Node fileNode = docNode.addNode(filename, "nt:file");
        Node jcrContent = fileNode.addNode("jcr:content", "nt:resource");
        InputStream inputStream = Utils.getFile(filename, type);
        jcrContent.setProperty("jcr:data", inputStream);
        jcrContent.setProperty("jcr:lastModified", Calendar.getInstance());
        jcrContent.setProperty("jcr:encoding", "UTF-8");
        if (filename.endsWith(".mp4")) {
          jcrContent.setProperty("jcr:mimeType", "video/mp4");
        }
        session.save();
        if (!"root".equals(name)) {
          listenerService_.broadcast(FILE_CREATED_ACTIVITY, null, fileNode);
        }

      }


    }
    catch (Exception e)
    {
      System.out.println("JCR::" + e.getMessage());
    }
    endSession();
  }

  private static String getSpacePath(String space)
  {
    return "Groups/spaces/"+space;
  }

/*
  public SessionProvider getUserSessionProvider() {
    SessionProvider sessionProvider = sessionProviderService_.getSessionProvider(null);
    return sessionProvider;
  }
*/

  protected SessionProvider startSessionAs(String user) {
    Identity identity = new Identity(user);

    try {
      Collection<MembershipEntry> membershipEntries = new ArrayList<MembershipEntry>();
      membershipEntries.add(new MembershipEntry("/spaces/social_intranet", "member"));
      membershipEntries.add(new MembershipEntry("/spaces/human_resources", "member"));
      membershipEntries.add(new MembershipEntry("/spaces/marketing_analytics", "member"));
      membershipEntries.add(new MembershipEntry("/platform/administrators", "member"));
      identity.setMemberships(membershipEntries);
    } catch (Exception e) {
      log.info(e.getMessage());
    }
    ConversationState state = new ConversationState(identity);
    ConversationState.setCurrent(state);
    sessionProviderService_.setSessionProvider(null, new SessionProvider(state));
    return sessionProviderService_.getSessionProvider(null);
  }

  protected void endSession() {
    sessionProviderService_.removeSessionProvider(null);
    ConversationState.setCurrent(null);
  }
}
