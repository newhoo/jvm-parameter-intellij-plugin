package io.github.newhoo.jvm.util;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.impl.java.stubs.index.JavaAnnotationIndex;
import com.intellij.psi.search.GlobalSearchScope;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * AppUtils
 *
 * @author huzunrong
 * @since 1.0
 */
public final class AppUtils {

    public static Set<String> findDubboService(Project project) {
        Collection<PsiAnnotation> psiAnnotations = JavaAnnotationIndex.getInstance().get("Service", project, GlobalSearchScope.projectScope(project));
        return psiAnnotations.stream()
                             .filter(psiAnnotation -> "com.alibaba.dubbo.config.annotation.Service".equals(psiAnnotation.getQualifiedName()))
                             .map(psiAnnotation -> {
                                 PsiModifierList psiModifierList = (PsiModifierList) psiAnnotation.getParent();
                                 PsiElement psiElement = psiModifierList.getParent();
                                 return (psiElement instanceof PsiClass) ? (PsiClass) psiElement : null;
                             })
                             .filter(Objects::nonNull)
                             .map(psiClass -> {
                                 PsiClass[] interfaces = psiClass.getInterfaces();
                                 return interfaces.length == 0 ? psiClass.getQualifiedName() : interfaces[0].getQualifiedName();
                             })
                             .collect(Collectors.toSet());
    }
}