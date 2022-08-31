package com.github.binhlecong.androidscanner.inspections

import com.github.binhlecong.androidscanner.Config
import com.github.binhlecong.androidscanner.Helper
import com.github.binhlecong.androidscanner.utils.UastClassUtil
import com.intellij.codeInspection.*
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiMethodCallExpression
import org.jetbrains.rpc.LOG
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UMethod
import org.jetbrains.uast.visitor.UastVisitor

class MethodInspection : AbstractBaseUastLocalInspectionTool(UMethod::class.java){
    // private val tag = "AndroidScanner"
    private val rules = Helper.loadRules(Config.PATH, Config.TYPE_METHOD_PARAM)

    override fun checkMethod(
        method: UMethod,
        manager: InspectionManager,
        isOnTheFly: Boolean
    ): Array<ProblemDescriptor> {
        val issueList = arrayListOf<ProblemDescriptor>()

        method.accept(object : UastVisitor {
            // Required by interface
            override fun visitElement(node: UElement): Boolean {
                return false
            }

            override fun visitCallExpression(node: UCallExpression): Boolean {
                val sourcePsi = node.sourcePsi ?: return false
                for (rule in rules) {
                    if (node.valueArgumentCount == 0)
                        continue

                    val methodName = " ";
                    val className = "java.security.SecureRandom";
                    val paramIndex = "0".toInt()

                    val nodeMethodName = node.methodName ?: continue
                    val isInResources = UastClassUtil.isMethodInClass(manager, methodName, className)
                    if (nodeMethodName == " " && isInResources){
                        val argument = node.getArgumentForParameter(paramIndex)?.sourcePsi?.text ?: continue
                        val regex = Regex("(.*setSeed.*)")

                        if (argument.matches(regex)) {
                            val briefDescription = "\"SecureRandom\" seeds should not be predictable"
                            // val needFix = true
                            // var fixes = emptyArray<MethodParamQuickFix>()

    //                            if (needFix) {
    //                                fixes += MethodParamQuickFix(
    //                                    rule[Config.FIELD_FIX_NAME],
    //                                    paramIndex,
    //                                    rule[Config.FIELD_FIX_NEW],
    //                                )
    //                            }
                            issueList.add(
                                manager.createProblemDescriptor(
                                    sourcePsi,
                                    briefDescription,
                                    isOnTheFly,
                                    emptyArray(),
                                    ProblemHighlightType.WARNING,
                                )
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