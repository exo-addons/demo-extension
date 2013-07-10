package org.exoplatform.addons.populator.services;

import org.exoplatform.social.core.image.ImageUtils;
import org.exoplatform.social.core.model.AvatarAttachment;

import java.io.InputStream;

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

}
