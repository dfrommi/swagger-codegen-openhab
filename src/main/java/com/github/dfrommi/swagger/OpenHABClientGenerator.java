package com.github.dfrommi.swagger;

import io.swagger.codegen.CodegenModel;
import io.swagger.codegen.CodegenProperty;
import io.swagger.codegen.CodegenType;
import io.swagger.codegen.SupportingFile;
import io.swagger.codegen.languages.AbstractJavaCodegen;
import io.swagger.models.Model;

import java.util.Map;

public class OpenHABClientGenerator extends AbstractJavaCodegen {
    public OpenHABClientGenerator() {
        embeddedTemplateDir = templateDir = "oh-templates";

        apiTestTemplateFiles.clear();
        modelDocTemplateFiles.clear();
        apiDocTemplateFiles.clear();
    }

    public CodegenType getTag() {
        return CodegenType.CLIENT;
    }

    public String getName() {
        return "openhab";
    }

    public String getHelp() {
        return "Generates a Java client library for openHAB.";
    }

    @Override
    public void processOpts() {
        additionalProperties.put(JAVA8_MODE, true);
        setDateLibrary("java8");

        super.processOpts();

        importMapping.remove("ApiModelProperty");
        importMapping.remove("ApiModel");

        final String invokerFolder = (sourceFolder + '/' + invokerPackage).replace(".", "/");
        final String authFolder = (sourceFolder + '/' + invokerPackage + ".auth").replace(".", "/");

        supportingFiles.add(new SupportingFile("gsonBuilderFactory.mustache", invokerFolder, "GsonBuilderFactory.java"));
        supportingFiles.add(new SupportingFile("runtimeTypeAdapterFactory.mustache", invokerFolder, "RuntimeTypeAdapterFactory.java"));
        supportingFiles.add(new SupportingFile("apiException.mustache", invokerFolder, "ApiException.java"));
        supportingFiles.add(new SupportingFile("auth/authorizer.mustache", authFolder, "Authorizer.java"));
        supportingFiles.add(new SupportingFile("auth/oauthAuthorizer.mustache", authFolder, "OAuthAuthorizer.java"));
    }

    @Override
    public CodegenModel fromModel(String name, Model model, Map<String, Model> allDefinitions) {
        CodegenModel codegenModel = super.fromModel(name, model, allDefinitions);
        codegenModel.imports.remove("ApiModel");
        return codegenModel;
    }

    @Override
    public void postProcessModelProperty(CodegenModel model, CodegenProperty property) {
        super.postProcessModelProperty(model, property);

        model.imports.remove("ApiModelProperty");
        model.imports.remove("ApiModel");

        model.imports.add("SerializedName");
        model.imports.add("TypeAdapter");
        model.imports.add("JsonAdapter");
        model.imports.add("JsonReader");
        model.imports.add("JsonWriter");
        model.imports.add("IOException");
    }
}
