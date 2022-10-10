package com.karashok.demoroutercompiler;

import static javax.lang.model.element.Modifier.PUBLIC;

import com.google.auto.service.AutoService;
import com.karashok.demorouterannotation.DAutoParams;
import com.karashok.demoroutercompiler.utils.Constants;
import com.karashok.demoroutercompiler.utils.EmptyUtils;
import com.karashok.demoroutercompiler.utils.LoadExtraBuilder;
import com.karashok.demoroutercompiler.utils.LogUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-28-2022
 */
@AutoService(Processor.class)
@SupportedOptions(Constants.ARGUMENTS_NAME)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes(Constants.ANN_TYPE_DAUTOPARAMS)
public class DAutoParamsProcessor extends AbstractProcessor {

  /** 节点工具类 (类、函数、属性都是节点) */
  private Elements elementUtil;

  /** type(类信息)工具类 */
  private Types typeUtil;

  /** 类/资源生成器 */
  private Filer filerUtil;

  /** 记录所有需要注入的属性 key:类节点 value:需要注入的属性节点集合 */
  private Map<TypeElement, List<Element>> parentAndChild = new HashMap<>();

  private LogUtils logUtil;

  /**
   * 初始化 从 {@link ProcessingEnvironment} 中获得一系列处理器工具
   *
   * @param processingEnvironment
   */
  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    logUtil = LogUtils.newLog(processingEnv.getMessager());
    elementUtil = processingEnv.getElementUtils();
    typeUtil = processingEnv.getTypeUtils();
    filerUtil = processingEnv.getFiler();
  }

  /**
   * @param set
   * @param roundEnvironment 表示当前或是之前的运行环境,可以通过该对象查找找到的注解。
   * @return true 表示后续处理器不会再处理(已经处理)
   */
  @Override
  public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
    if (!EmptyUtils.isEmpty(set)) {
      Set<? extends Element> elements =
          roundEnvironment.getElementsAnnotatedWith(DAutoParams.class);
      if (!EmptyUtils.isEmpty(elements)) {
        try {
          categories(elements);
          generateAutoWired();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      return true;
    }
    return false;
  }

  /**
   * 记录需要生成的类与属性
   *
   * @param elements
   * @throws IllegalAccessException
   */
  private void categories(Set<? extends Element> elements) throws IllegalAccessException {
    for (Element element : elements) {
      TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
      List<Element> aDefault =
          parentAndChild.getOrDefault(enclosingElement, new ArrayList<Element>());
      aDefault.add(element);
      parentAndChild.put(enclosingElement, aDefault);
    }
  }

  private void generateAutoWired() throws IOException {
    TypeMirror typeActivity = elementUtil.getTypeElement(Constants.ACTIVITY).asType();
    TypeElement IExtra = elementUtil.getTypeElement(Constants.IEXTRA);
    // 参数 Object target
    ParameterSpec parameterSpec = ParameterSpec.builder(TypeName.OBJECT, "target").build();
    if (!EmptyUtils.isEmpty(parentAndChild)) {
      // 遍历所有需要注入的 类:属性
      for (Map.Entry<TypeElement, List<Element>> entry : parentAndChild.entrySet()) {
        TypeElement entryKey = entry.getKey();
        if (!typeUtil.isSubtype(entryKey.asType(), typeActivity)) {
          throw new RuntimeException("[Just Support Activity Field]:" + entryKey);
        }
        //封装的函数生成类
        LoadExtraBuilder loadExtraBuilder = new LoadExtraBuilder(parameterSpec);
        loadExtraBuilder.setElementUtils(elementUtil);
        loadExtraBuilder.setTypeUtils(typeUtil);
        ClassName className = ClassName.get(entryKey);
        loadExtraBuilder.injectTarget(className);
        // 遍历属性
        for (Element element : entry.getValue()) {
          loadExtraBuilder.buildStatement(element);
        }
        // 生成java类名
        String extraClassName = entryKey.getSimpleName() + Constants.NAME_OF_EXTRA;
        // 生成 XX$$Autowired
        JavaFile.builder(
                className.packageName(),
                TypeSpec.classBuilder(extraClassName)
                    .addSuperinterface(ClassName.get(IExtra))
                    .addModifiers(PUBLIC)
                    .addMethod(loadExtraBuilder.build())
                    .build())
            .build()
            .writeTo(filerUtil);
        logUtil.i("Generated Extra: " + className.packageName() + "." + extraClassName);
      }
    }
  }
}
