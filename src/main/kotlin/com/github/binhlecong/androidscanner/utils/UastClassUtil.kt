package com.github.binhlecong.androidscanner.utils

import com.intellij.codeInspection.InspectionManager
import com.intellij.psi.PsiManager
import com.intellij.psi.util.ClassUtil
import org.jetbrains.uast.UCallExpression

class UastClassUtil {
    companion object {
        fun isMethodInClass(manager: InspectionManager, memberName: String, className: String): Boolean {
            val psiManager = PsiManager.getInstance(manager.project)
            val containingClass = ClassUtil.findPsiClass(psiManager, className) ?: return false
            val methodsFound = containingClass.findMethodsByName(memberName, true)
            return methodsFound.isNotEmpty()
        }

        fun getVarNameFromDeclaration(node: UCallExpression): String {
            for (element in node.uastParent!!.javaPsi!!.children) {
                if (element.text.equals("=")) {
                    return element.prevSibling.text.ifBlank {
                        element.prevSibling.prevSibling.text
                    }
                }
            }
            return ""
        }

        fun getParamText(node: UCallExpression, index: Int): String {
            return node.getArgumentForParameter(index)?.javaPsi?.text ?: ""
        }
    }
}