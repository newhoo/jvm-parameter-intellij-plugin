package io.github.newhoo.jvm.util;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * AppUtils
 *
 * @author huzunrong
 * @since 1.0
 */
public final class AppUtils {

    public static Set<String> findDubboService(Project project) {
        PsiManager psiManager = PsiManager.getInstance(project);
        Collection<VirtualFile> containingFiles = FileTypeIndex.getFiles(JavaFileType.INSTANCE, GlobalSearchScope.projectScope(project));
        return containingFiles.stream()
                              .flatMap(virtualFile -> Stream.of(((PsiJavaFile) psiManager.findFile(virtualFile)).getClasses()))
                              .filter(psiClass -> {
                                  for (PsiAnnotation annotation : psiClass.getAnnotations()) {
                                      if ("com.alibaba.dubbo.config.annotation.Service".equals(annotation.getQualifiedName())) {
                                          return true;
                                      }
                                  }
                                  return false;
                              })
                              .map(psiClass -> {
                                  PsiClass[] interfaces = psiClass.getInterfaces();
                                  return interfaces.length == 0 ? psiClass.getQualifiedName() : interfaces[0].getQualifiedName();
                              })
                              .collect(Collectors.toSet());
    }
}