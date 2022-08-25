package com.github.binhlecong.androidscanner.inspections

import com.github.binhlecong.androidscanner.utils.UastClassUtil
import com.intellij.codeInspection.AbstractBaseUastLocalInspectionTool
import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UMethod
import org.jetbrains.uast.visitor.UastVisitor

class MyInspection : AbstractBaseUastLocalInspectionTool(UMethod::class.java) {
    private val tag = "AndroidScanner"

    override fun checkMethod(
        method: UMethod,
        manager: InspectionManager,
        isOnTheFly: Boolean,
    ): Array<ProblemDescriptor> {
        val issueList = arrayListOf<ProblemDescriptor>()
        method.accept(object : UastVisitor {
            // Required by interface
            override fun visitElement(node: UElement): Boolean {
                return false
            }

            override fun visitCallExpression(node: UCallExpression): Boolean {
                if (node.valueArgumentCount == 0)
                    return false
                val sourcePsi = node.sourcePsi ?: return false

                // TODO: get these value from xml file
                val methodName = "getInstance"
                val className = "java.security.MessageDigest"
                val paramIndex = 0
                val pattern = "(.*SHA1.*)"

                val nodeMethodName = node.methodName ?: return false
                if (nodeMethodName == methodName &&
                    node.valueArgumentCount > paramIndex &&
                    UastClassUtil.isMethodInClass(manager, nodeMethodName, className)
                ) {
                    val param = node.getArgumentForParameter(paramIndex)?.sourcePsi?.text ?: return false
                    val reg = Regex(pattern)
                    if (param.matches(reg)) {
                        val description = "$tag SHA1 detected at: ${node.methodName}"

                        issueList.add(
                            manager.createProblemDescriptor(
                                sourcePsi,
                                description,
                                isOnTheFly,
                                emptyArray(),
                                ProblemHighlightType.WARNING,
                            ),
                        )
                    }
                }
                return false
            }
        })

        return issueList.toTypedArray()
    }
}