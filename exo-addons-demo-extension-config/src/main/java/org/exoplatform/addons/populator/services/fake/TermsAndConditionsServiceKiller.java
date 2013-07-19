package org.exoplatform.addons.populator.services.fake;

import org.exoplatform.commons.api.settings.SettingService;
import org.exoplatform.commons.api.settings.SettingValue;
import org.exoplatform.commons.api.settings.data.Context;
import org.exoplatform.commons.api.settings.data.Scope;
import org.exoplatform.platform.common.account.setup.web.AccountSetup;
import org.exoplatform.platform.welcomescreens.service.TermsAndConditionsService;

public class TermsAndConditionsServiceKiller implements TermsAndConditionsService {

  public TermsAndConditionsServiceKiller(SettingService settingService)
  {
    if(settingService.get(Context.GLOBAL, Scope.GLOBAL, AccountSetup.ACCOUNT_SETUP_NODE)==null)
      settingService.set(Context.GLOBAL, Scope.GLOBAL, AccountSetup.ACCOUNT_SETUP_NODE, SettingValue.create("setup over:" + "true"));
  }

  @Override
  public boolean isTermsAndConditionsChecked() {
    return true;
  }

  @Override
  public void checkTermsAndConditions() {
  }
}
