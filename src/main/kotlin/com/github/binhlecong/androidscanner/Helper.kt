package com.github.binhlecong.androidscanner

import javax.swing.table.TableModel

class Helper {
    companion object {
        fun getField(rules: List<List<String>>, field: Int): List<String> {
//            val res: MutableList<String> = mutableListOf()
//            for (rule in rules)
//                res.add(rule[field])
//            return res.distinct()
            return emptyList()
        }

        fun loadRules(path: String, type: String): List<List<String>> {
//            val res: MutableList<List<String>> = mutableListOf()
//            val item: MutableList<String> = mutableListOf()
//            val inputStream = File(path).inputStream()
//            val factory = XmlPullParserFactory.newInstance()
//            factory.isNamespaceAware = true
//            val parser = factory.newPullParser()
//            parser.setInput(inputStream, null)
//            var eventType = parser.eventType
//            var text: String? = null
//            while (eventType != XmlPullParser.END_DOCUMENT) {
//                val tagName = parser.name
//                when (eventType) {
//                    XmlPullParser.START_TAG -> {
//                        if (tagName.equals("rule", ignoreCase = true))
//                            item.clear()
//                    }
//
//                    XmlPullParser.TEXT -> {
//                        text = parser.text
//                    }
//
//                    XmlPullParser.END_TAG -> {
//                        if (tagName.equals("rule", ignoreCase = true)) {
//                            if (item[Config.FIELD_TYPE] == type || type == "")
//                                res.add(item.toList())
//                        } else {
//                            if (text != null)
//                                item.add(text)
//                        }
//                    }
//                }
//
//                eventType = parser.next()
//            }
//            return res
            return emptyList()
        }

        fun saveRules(path: String, model: TableModel) {
//            val outputStream = File(path).outputStream()
//            outputStream.write("<rules>\n".toByteArray())
//            for (countRow in 0 until model.rowCount) {
//                if (model.getValueAt(countRow, 0).toString().isEmpty())
//                    continue
//
//                outputStream.write("\t<rule>\n".toByteArray())
//                for (countCol in 0 until model.columnCount) {
//                    outputStream.write(("\t\t<" + Config.FIELD_NAMES[countCol] + ">").toByteArray())
//                    outputStream.write(model.getValueAt(countRow, countCol).toString().trim().toByteArray())
//                    outputStream.write(("</" + Config.FIELD_NAMES[countCol] + ">\n").toByteArray())
//                }
//                outputStream.write("\t</rule>\n".toByteArray())
//            }
//            outputStream.write("</rules>".toByteArray())
        }

        fun convertListListToArrayArray(dataList: List<List<String>>): Array<Array<String>?> {
//            val res: Array<Array<String>?> = arrayOfNulls(dataList.size)
//
//            var i = 0
//            while (i < dataList.size) {
//                res[i] = dataList[i].toTypedArray()
//                i++
//            }
//            return res
            return emptyArray()
        }
    }
}