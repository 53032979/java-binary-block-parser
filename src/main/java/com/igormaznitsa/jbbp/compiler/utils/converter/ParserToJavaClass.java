/*
 * Copyright 2017 Igor Maznitsa.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.igormaznitsa.jbbp.compiler.utils.converter;

import com.igormaznitsa.jbbp.JBBPParser;
import com.igormaznitsa.jbbp.compiler.JBBPCompiledBlock;
import com.igormaznitsa.jbbp.compiler.JBBPNamedFieldInfo;
import com.igormaznitsa.jbbp.compiler.tokenizer.JBBPFieldTypeParameterContainer;
import com.igormaznitsa.jbbp.compiler.varlen.JBBPIntegerValueEvaluator;
import com.igormaznitsa.jbbp.io.JBBPBitInputStream;
import com.igormaznitsa.jbbp.io.JBBPBitOrder;
import com.igormaznitsa.jbbp.io.JBBPByteOrder;
import com.igormaznitsa.jbbp.utils.JBBPUtils;

import java.io.IOException;

public class ParserToJavaClass extends AbstractCompiledBlockConverter<ParserToJavaClass> {

    private static final String TEMPLATE;

    static {
        JBBPBitInputStream stream = null;
        try {
            stream = new JBBPBitInputStream(ParserToJavaClass.class.getResourceAsStream("/templates/template_java_class.txt"));
            TEMPLATE = new String(stream.readByteArray(-1), "UTF-8");
        } catch (IOException ex) {
            throw new Error("Can't load template", ex);
        } finally {
            JBBPUtils.closeQuietly(stream);
        }
    }

    private final String packageName;
    private final String className;
    private final TextBuffer methods = new TextBuffer();
    private final TextBuffer fields = new TextBuffer();
    private final TextBuffer classComments = new TextBuffer();
    private final String extraMethods;
    private final JBBPBitOrder bitOrder;
    private boolean detectedCustomFields;
    private boolean detectedVarFields;

    private String result;

    public ParserToJavaClass(final JBBPBitOrder notNullBitOrder, final String packageName, final String className, final JBBPParser notNullParser, final String nullableExtraMethods) {
        this(notNullBitOrder, packageName, className, notNullParser.getCompiledBlock(), nullableExtraMethods);
    }

    public ParserToJavaClass(final JBBPBitOrder notNullBitOrder, final String packageName, final String className, final JBBPCompiledBlock notNullCompiledBlock, final String nullableExtraMethods) {
        super(notNullCompiledBlock);
        this.bitOrder = notNullBitOrder;
        this.packageName = packageName;
        this.className = className;
        this.extraMethods = nullableExtraMethods == null ? "" : nullableExtraMethods;
    }

    @Override
    public void onConvertStart() {
        this.detectedCustomFields = false;
        this.detectedVarFields = false;
        this.classComments.print("// Generated by ").print(ParserToJavaClass.class.getName());
    }

    public String getResult() {
        return this.result;
    }

    @Override
    public void onConvertEnd() {
        this.result = TEMPLATE
                .replace("${extraMethods}", this.extraMethods)
                .replace("${packageName}", this.packageName)
                .replace("${className}", this.className)
                .replace("${classComments}", this.classComments.toStringAndClean())
                .replace("${fields}", this.fields.toStringAndClean())
                .replace("${methods}", this.methods.toStringAndClean())
                .replace("${constructors}", makeConstructors())
                .replace("${bitOrder}", "JBBPBitOrder." + this.bitOrder.name());
    }

    private String makeConstructors() {
        final TextBuffer buffer = new TextBuffer();

        if (!this.detectedCustomFields && !this.detectedVarFields) {

        } else {

        }

        return buffer.toString();
    }

    @Override
    public void onStructEnd(int offsetInCompiledBlock, JBBPNamedFieldInfo nullableNameFieldInfo) {
    }

    @Override
    public void onStructStart(int offsetInCompiledBlock, JBBPNamedFieldInfo nullableNameFieldInfo, JBBPIntegerValueEvaluator nullableArraySize) {
    }

    @Override
    public void onBitField(int offsetInCompiledBlock, JBBPNamedFieldInfo nullableNameFieldInfo, JBBPByteOrder byteOrder, JBBPIntegerValueEvaluator notNullFieldSize, JBBPIntegerValueEvaluator nullableArraySize) {
    }

    @Override
    public void onCustom(int offsetInCompiledBlock, JBBPFieldTypeParameterContainer notNullfieldType, JBBPNamedFieldInfo nullableNameFieldInfo, JBBPByteOrder byteOrder, JBBPIntegerValueEvaluator nullableArraySize) {
        this.detectedCustomFields = true;
    }

    @Override
    public void onVar(int offsetInCompiledBlock, JBBPNamedFieldInfo nullableNameFieldInfo, JBBPByteOrder byteOrder, JBBPIntegerValueEvaluator nullableArraySize) {
        this.detectedVarFields = true;
    }

    @Override
    public void onPrimitive(int offsetInCompiledBlock, int primitiveType, JBBPNamedFieldInfo nullableNameFieldInfo, JBBPByteOrder byteOrder, JBBPIntegerValueEvaluator nullableArraySize) {

    }

    @Override
    public void onActionItem(int offsetInCompiledBlock, int actionType, JBBPIntegerValueEvaluator nullableArgument) {
    }

}
