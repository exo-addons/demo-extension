package org.exoplatform.addons.populator.services;

import org.apache.commons.io.IOUtils;
import org.exoplatform.social.core.image.ImageUtils;
import org.exoplatform.social.core.model.AvatarAttachment;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class Utils {

  public static AvatarAttachment getAvatarAttachment(String fileName) throws Exception
  {
    String mimeType = "image/png";
    int WIDTH = 200;
    InputStream inputStream = Utils.class.getClassLoader().getResourceAsStream("/medias/images/"+fileName);

    // Resize avatar to fixed width if can't(avatarAttachment == null) keep
    // origin avatar
    AvatarAttachment avatarAttachment = ImageUtils.createResizedAvatarAttachment(inputStream,
            WIDTH,
            0,
            null,
            fileName,
            mimeType,
            null);
    if (avatarAttachment == null) {
      avatarAttachment = new AvatarAttachment(null,
              fileName,
              mimeType,
              inputStream,
              null,
              System.currentTimeMillis());
    }

    return avatarAttachment;
  }

  public static String getWikiPage(String fileName) throws Exception
  {
    InputStream inputStream = getLocalFile(fileName, "/medias/contents");
    StringWriter writer = new StringWriter();
    IOUtils.copy(inputStream, writer);

    return writer.toString();
  }

  public static InputStream getDocument(String fileName)
  {
    return getLocalFile(fileName, "/medias/documents");
  }

  public static InputStream getTemplate(String fileName)
  {
    return getLocalFile(fileName, "/medias/templates");
  }

  public static String getData(String fileName)
  {
    String out = "";
    InputStream inputStream = getLocalFile(fileName, "/data");

    StringWriter writer = new StringWriter();
    try {
      IOUtils.copy(inputStream, writer);
      out = writer.toString();

    } catch (IOException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }

    return out;
  }

  public static InputStream getFile(String fileName, String folder)
  {
    return getLocalFile(fileName, "/medias/"+folder);
  }

  private static InputStream getLocalFile(String fileName, String folder)
  {
    InputStream inputStream = Utils.class.getClassLoader().getResourceAsStream(folder+"/"+fileName);

    return inputStream;
  }

}
