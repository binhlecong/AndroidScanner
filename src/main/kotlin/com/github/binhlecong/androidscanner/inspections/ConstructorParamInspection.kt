package com.github.binhlecong.androidscanner.inspections

import com.github.binhlecong.androidscanner.Config
import com.github.binhlecong.androidscanner.Helper
import com.github.binhlecong.androidscanner.utils.UastClassUtil
import com.github.binhlecong.androidscanner.utils.UastQuickFix
import com.intellij.codeInspection.AbstractBaseUastLocalInspectionTool
import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UMethod
import org.jetbrains.uast.visitor.UastVisitor


class ConstructorParamInspection : AbstractBaseUastLocalInspectionTool(UMethod::class.java) {
    private val tag = "AndroidScanner"
    private val rules = Helper.loadRules(Config.PATH, Config.TYPE_CONSTRUCTOR_PARAM)

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

                    val className = rule[Config.FIELD_CLASS_NAME]
                    val paramIndex = rule[Config.FIELD_PARAM_INDEX].toInt()

                    //val nodeMethodName = node.methodName ?: continue
                    val nodeClassReference = node.classReference ?: continue
                    if (className.split('.').last() == nodeClassReference.toString() &&
                        node.valueArgumentCount > paramIndex
                    ) {
                        val argument = node.getArgumentForParameter(paramIndex)?.sourcePsi?.text ?: continue
                        val regex = Regex(rule[Config.FIELD_PARAM_PATTERN])
                        if (argument.matches(regex)) {
                            val briefDescription = rule[Config.FIELD_BRIEF_DESCRIPTION]
                            val needFix = rule[Config.FIELD_NEED_FIX].trim() == "1"
                            var fixes = emptyArray<UastQuickFix>()

                            if (needFix) {
                                val paramText = UastClassUtil.getParamText(node, paramIndex)
                                fixes += UastQuickFix(
                                    rule[Config.FIELD_FIX_NAME],
                                    rule[Config.FIELD_FIX_OLD],
                                    rule[Config.FIELD_FIX_NEW],
                                    paramText
                                )
                            }
                            issueList.add(
                                manager.createProblemDescriptor(
                                    sourcePsi,
                                    briefDescription,
                                    isOnTheFly,
                                    fixes,
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