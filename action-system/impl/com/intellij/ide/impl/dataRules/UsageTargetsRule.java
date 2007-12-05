package com.intellij.ide.impl.dataRules;

import com.intellij.find.FindManager;
import com.intellij.find.findUsages.PsiElement2UsageTargetAdapter;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.actionSystem.DataProvider;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.usages.UsageTarget;
import org.jetbrains.annotations.Nullable;

/**
 * @author max
 */
public class UsageTargetsRule implements GetDataRule {
  @Nullable
  public Object getData(DataProvider dataProvider) {
    PsiElement psiElement = (PsiElement)dataProvider.getData(DataConstants.PSI_ELEMENT);
    if (psiElement instanceof PsiDirectory) psiElement = JavaDirectoryService.getInstance().getPackage(((PsiDirectory)psiElement));
    if (psiElement instanceof NavigationItem) {
      if (!FindManager.getInstance(psiElement.getProject()).canFindUsages(psiElement)) return null;
      return new UsageTarget[] {new PsiElement2UsageTargetAdapter(psiElement)};
    }
    return null;
  }
}
