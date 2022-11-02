package com.github.binhlecong.androidscanner.inspections

import com.github.binhlecong.androidscanner.visitors.CallExpressionVisitor
import com.intellij.codeInspection.AbstractBaseUastLocalInspectionTool
import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.psi.PsiFile
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UExpression
import org.jetbrains.uast.UFile
import org.jetbrains.uast.toUElement
import org.jetbrains.uast.visitor.UastVisitor

class EntryPointInspection : AbstractBaseUastLocalInspectionTool(UFile::class.java) {
    //private val rules = Helper.loadRules(Config.PATH, Config.TYPE_CONSTRUCTOR)

//    fun loadRules() {
//
//    }

    override fun checkFile(file: PsiFile, manager: InspectionManager, isOnTheFly: Boolean): Array<ProblemDescriptor> {
        val uFile = file.toUElement(UFile::class.java) ?: return ProblemDescriptor.EMPTY_ARRAY
        val issueList = arrayListOf<ProblemDescriptor>()

        uFile.accept(object : UastVisitor {
            override fun visitElement(node: UElement): Boolean {
                return false
            }

            override fun visitExpression(node: UExpression): Boolean {
                node.accept(CallExpressionVisitor(manager, issueList, isOnTheFly))
                return false
            }
        })

//        uFile.accept(object : UastVisitor {
//            override fun visitElement(node: UElement): Boolean {
//                val sourcePsi = node.sourcePsi ?: return false
//
//                issueList.add(
//                    manager.createProblemDescriptor(
//                        sourcePsi,
//                        "Element: ${node::class.simpleName}: ${node.asSourceString()}",
//                        isOnTheFly,
//                        LocalQuickFix.EMPTY_ARRAY,
//                        ProblemHighlightType.WARNING,
//                    )
//                )
//                return false
//            }

//            override fun visitCallExpression(node: UCallExpression): Boolean {
//                val sourcePsi = node.sourcePsi ?: return false
//                val elements = node.withContainingElements
//                for (e in elements) {
//                    issueList.add(
//                        manager.createProblemDescriptor(
//                            sourcePsi,
//                            "ContainElement: ${e::class.simpleName}: ${node.asSourceString()}",
//                            isOnTheFly,
//                            LocalQuickFix.EMPTY_ARRAY,
//                            ProblemHighlightType.WARNING,
//                        )
//                    )
//                }
//                issueList.add(
//                    manager.createProblemDescriptor(
//                        sourcePsi,
//                        "CallExpression: ${node.asSourceString()}",
//                        isOnTheFly,
//                        LocalQuickFix.EMPTY_ARRAY,
//                        ProblemHighlightType.WARNING,
//                    )
//                )
//                return false
//            }

//            override fun visitVariable(node: UVariable): Boolean {
//                val sourcePsi = node.sourcePsi ?: return false
//                issueList.add(
//                    manager.createProblemDescriptor(
//                        sourcePsi,
//                        "Variable: ${node.asSourceString()}",
//                        isOnTheFly,
//                        LocalQuickFix.EMPTY_ARRAY,
//                        ProblemHighlightType.WARNING,
//                    )
//                )
//                return false
//            }
//
//            override fun visitParameter(node: UParameter): Boolean {
//                val sourcePsi = node.sourcePsi ?: return false
//                issueList.add(
//                    manager.createProblemDescriptor(
//                        sourcePsi,
//                        "Parameter: ${node.asSourceString()}",
//                        isOnTheFly,
//                        LocalQuickFix.EMPTY_ARRAY,
//                        ProblemHighlightType.WARNING,
//                    )
//                )
//                return false
//            }
//
//            override fun visitLiteralExpression(node: ULiteralExpression): Boolean {
//                val sourcePsi = node.sourcePsi ?: return false
//                issueList.add(
//                    manager.createProblemDescriptor(
//                        sourcePsi,
//                        "LiteralExpression: ${node.asSourceString()}",
//                        isOnTheFly,
//                        LocalQuickFix.EMPTY_ARRAY,
//                        ProblemHighlightType.WARNING,
//                    )
//                )
//                return false
//            }
//
//            override fun visitInitializer(node: UClassInitializer): Boolean {
//                val sourcePsi = node.sourcePsi ?: return false
//                issueList.add(
//                    manager.createProblemDescriptor(
//                        sourcePsi,
//                        "Initializer: ${node.asSourceString()}",
//                        isOnTheFly,
//                        LocalQuickFix.EMPTY_ARRAY,
//                        ProblemHighlightType.WARNING,
//                    )
//                )
//                return false
//            }
//
//            override fun visitDeclarationsExpression(node: UDeclarationsExpression): Boolean {
//                val sourcePsi = node.sourcePsi ?: return false
//                issueList.add(
//                    manager.createProblemDescriptor(
//                        sourcePsi,
//                        "DeclarationsExpression: ${node.asSourceString()}",
//                        isOnTheFly,
//                        LocalQuickFix.EMPTY_ARRAY,
//                        ProblemHighlightType.WARNING,
//                    )
//                )
//                return false
//            }
//
//            override fun visitQualifiedReferenceExpression(node: UQualifiedReferenceExpression): Boolean {
//                val sourcePsi = node.sourcePsi ?: return false
//                issueList.add(
//                    manager.createProblemDescriptor(
//                        sourcePsi,
//                        "QualifiedReferenceExpression: ${node.asSourceString()}",
//                        isOnTheFly,
//                        LocalQuickFix.EMPTY_ARRAY,
//                        ProblemHighlightType.WARNING,
//                    )
//                )
//                return false
//            }
//
//            override fun visitSimpleNameReferenceExpression(node: USimpleNameReferenceExpression): Boolean {
//                val sourcePsi = node.sourcePsi ?: return false
//                issueList.add(
//                    manager.createProblemDescriptor(
//                        sourcePsi,
//                        "SimpleNameReferenceExpression: ${node.asSourceString()}",
//                        isOnTheFly,
//                        LocalQuickFix.EMPTY_ARRAY,
//                        ProblemHighlightType.WARNING,
//                    )
//                )
//                return false
//            }
//
//            override fun visitTypeReferenceExpression(node: UTypeReferenceExpression): Boolean {
//                val sourcePsi = node.sourcePsi ?: return false
//                issueList.add(
//                    manager.createProblemDescriptor(
//                        sourcePsi,
//                        "TypeReferenceExpression: ${node.asSourceString()}",
//                        isOnTheFly,
//                        LocalQuickFix.EMPTY_ARRAY,
//                        ProblemHighlightType.WARNING,
//                    )
//                )
//                return false
//            }
//
//            override fun visitImportStatement(node: UImportStatement): Boolean {
//                val sourcePsi = node.sourcePsi ?: return false
//                issueList.add(
//                    manager.createProblemDescriptor(
//                        sourcePsi,
//                        "ImportStatement: ${node.asSourceString()}",
//                        isOnTheFly,
//                        LocalQuickFix.EMPTY_ARRAY,
//                        ProblemHighlightType.WARNING,
//                    )
//                )
//                return false
//            }
//
//            override fun visitDeclaration(node: UDeclaration): Boolean {
//                val sourcePsi = node.sourcePsi ?: return false
//                issueList.add(
//                    manager.createProblemDescriptor(
//                        sourcePsi,
//                        "Declaration: ${node.asSourceString()}",
//                        isOnTheFly,
//                        LocalQuickFix.EMPTY_ARRAY,
//                        ProblemHighlightType.WARNING,
//                    )
//                )
//                return false
//            }
//
//            override fun visitMethod(node: UMethod): Boolean {
//                val sourcePsi = node.sourcePsi ?: return false
//                issueList.add(
//                    manager.createProblemDescriptor(
//                        sourcePsi,
//                        "Method: ${node.asSourceString()}",
//                        isOnTheFly,
//                        LocalQuickFix.EMPTY_ARRAY,
//                        ProblemHighlightType.WARNING,
//                    )
//                )
//                return false
//            }
//
//
//            override fun visitField(node: UField): Boolean {
//                val sourcePsi = node.sourcePsi ?: return false
//                issueList.add(
//                    manager.createProblemDescriptor(
//                        sourcePsi,
//                        "Field: ${node.asSourceString()}",
//                        isOnTheFly,
//                        LocalQuickFix.EMPTY_ARRAY,
//                        ProblemHighlightType.WARNING,
//                    )
//                )
//                return false
//            }
//
//            override fun visitLocalVariable(node: ULocalVariable): Boolean {
//                val sourcePsi = node.sourcePsi ?: return false
//                issueList.add(
//                    manager.createProblemDescriptor(
//                        sourcePsi,
//                        "LocalVariable: ${node.asSourceString()}",
//                        isOnTheFly,
//                        LocalQuickFix.EMPTY_ARRAY,
//                        ProblemHighlightType.WARNING,
//                    )
//                )
//                return false
//            }
//        })

        return issueList.toTypedArray()
    }
}