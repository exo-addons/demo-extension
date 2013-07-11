package org.exoplatform.addons.populator.services;

import org.apache.commons.io.IOUtils;
import org.exoplatform.social.core.image.ImageUtils;
import org.exoplatform.social.core.model.AvatarAttachment;

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
    InputStream inputStream = Utils.class.getClassLoader().getResourceAsStream("/medias/contents/"+fileName);
    StringWriter writer = new StringWriter();
    IOUtils.copy(inputStream, writer);

    return writer.toString();
  }

}
