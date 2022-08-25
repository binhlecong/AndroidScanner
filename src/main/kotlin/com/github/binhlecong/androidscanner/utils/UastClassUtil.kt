package com.github.binhlecong.androidscanner.utils

import com.intellij.codeInspection.InspectionManager
import com.intellij.psi.PsiManager
import com.intellij.psi.util.ClassUtil

class UastClassUtil {
    companion object {
        fun isMethodInClass(manager: InspectionManager, memberName: String, className: String): Boolean {
            val psiManager = PsiManager.getInstance(manager.project)
            val containingClass = ClassUtil.findPsiClass(psiManager, className) ?: return false
            val methodsFound = containingClass.findMethodsByName(memberName, true)
            return methodsFound.isNotEmpty()
        }
    }
}