package com.github.binhlecong.androidscanner.inspections

import com.intellij.codeInspection.AbstractBaseUastLocalInspectionTool
import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UField
import org.jetbrains.uast.UMethod
import org.jetbrains.uast.visitor.UastVisitor

class MyInspection : AbstractBaseUastLocalInspectionTool(UField::class.java, UMethod::class.java) {

    private val tag = "AndroidScanner"

    override fun checkMethod(
        method: UMethod,
        manager: InspectionManager,
        isOnTheFly: Boolean,
    ): Array<ProblemDescriptor> {
        // To reuse 9Fix rule set, we need:
        // - method call expressions in each method of a class: yes
        // - argument list of those methods call: yes
        // - the class that the method call expressions belong to: no

        val issueList = arrayListOf<ProblemDescriptor>()
        method.accept(object : UastVisitor {
            // Required by interface
            override fun visitElement(node: UElement): Boolean {
                return false
            }

            // Visit all method call expressions in a method
            override fun visitCallExpression(node: UCallExpression): Boolean {
                if (node.valueArgumentCount == 0)
                    return false

                val isInResources = node.classReference
                val paramIndex = 0
                if (node.methodName == "getInstance") {
                    if (node.valueArgumentCount < paramIndex + 1)
                        return false

                    val param = node.getArgumentForParameter(paramIndex)!!.sourcePsi!!.text
                    val pattern = "(.*SHA1.*)"
                    val reg = Regex(pattern)
                    if (param.matches(reg)) {
                        val sourcePsi = node.sourcePsi
                        val description = "$tag SHA1 detected at: ${node.methodName}"

                        if (sourcePsi != null) {
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
                }
                return false
            }
        })

        return issueList.toTypedArray()
    }
}