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


class MethodParamInspection : AbstractBaseUastLocalInspectionTool(UMethod::class.java) {
    private val tag = "AndroidScanner"
    private val rules = Helper.loadRules(Config.PATH, Config.TYPE_METHOD_PARAM)

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
                val sourcePsi = node.sourcePsi ?: return false
                for (rule in rules) {
                    if (node.valueArgumentCount == 0)
                        continue

                    val methodName = rule[Config.FIELD_METHOD_NAME]
                    val className = rule[Config.FIELD_CLASS_NAME]
                    val paramIndex = rule[Config.FIELD_PARAM_INDEX].toInt()

                    val nodeMethodName = node.methodName ?: continue
                    if (nodeMethodName == methodName &&
                        node.valueArgumentCount > paramIndex &&
                        UastClassUtil.isMethodInClass(manager, nodeMethodName, className)
                    ) {
                        val argument = node.getArgumentForParameter(paramIndex)?.sourcePsi?.text ?: continue
                        val regex = Regex(rule[Config.FIELD_PARAM_PATTERN])
                        if (argument.matches(regex)) {
                            val briefDescription = rule[Config.FIELD_BRIEF_DESCRIPTION]
                            val needFix = rule[Config.FIELD_NEED_FIX].trim() == "1"
                            if (needFix) {
                                val fixes = arrayOf(
                                    MethodParamQuickFix(
                                        rule[Config.FIELD_FIX_NAME],
                                        paramIndex,
                                        rule[Config.FIELD_FIX_NEW],
                                    )
                                )

                                issueList.add(
                                    manager.createProblemDescriptor(
                                        sourcePsi,
                                        briefDescription,
                                        isOnTheFly,
                                        fixes,
                                        ProblemHighlightType.WARNING,
                                    ),
                                )
                            } else {
                                issueList.add(
                                    manager.createProblemDescriptor(
                                        sourcePsi,
                                        briefDescription,
                                        isOnTheFly,
                                        emptyArray(),
                                        ProblemHighlightType.WARNING,
                                    ),
                                )
                            }
                        }
                    }
                }
                return false
            }
        })

        return issueList.toTypedArray()
    }
}

class MethodParamQuickFix(private val fixName: String, private val paramIndex: Int, private val newFix: String) :
    LocalQuickFix {
    override fun getFamilyName(): String {
        return fixName
    }

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        try {
            val factory = JavaPsiFacade.getInstance(project).elementFactory
            val node = descriptor.psiElement as PsiMethodCallExpression
            val param = node.argumentList.findElementAt(paramIndex)
            val newUElement = factory.createExpressionFromText(newFix, null)
            param?.replace(newUElement)
        } catch (e: Exception) {
            LOG.error(e)
        }
    }
}