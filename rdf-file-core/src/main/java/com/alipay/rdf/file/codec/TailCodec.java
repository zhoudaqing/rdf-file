package com.alipay.rdf.file.codec;

import java.util.List;
import java.util.Map;

import com.alipay.rdf.file.interfaces.FileReader;
import com.alipay.rdf.file.interfaces.FileWriter;
import com.alipay.rdf.file.loader.ProtocolLoader;
import com.alipay.rdf.file.loader.TemplateLoader;
import com.alipay.rdf.file.meta.FileColumnMeta;
import com.alipay.rdf.file.meta.FileMeta;
import com.alipay.rdf.file.model.FileConfig;
import com.alipay.rdf.file.model.FileDataTypeEnum;
import com.alipay.rdf.file.processor.ProcessorTypeEnum;
import com.alipay.rdf.file.protocol.ProtocolDefinition;
import com.alipay.rdf.file.protocol.RowDefinition;
import com.alipay.rdf.file.spi.RdfFileProcessorSpi;

/**
 * Copyright (C) 2013-2018 Ant Financial Services Group
 *
 * @author hongwei.quhw
 * @version $Id: TailCodec.java, v 0.1 2018年3月12日 下午4:14:12 hongwei.quhw Exp $
 */
public class TailCodec implements FileCodec {
    public static TailCodec instance = new TailCodec();

    /** 
     * @see hongwei.quhw.file.codec.FileCodec#serialize(Object, hongwei.quhw.file.common.ProtocolFileWriter)
     */
    @Override
    public void serialize(Object bean, FileConfig fileConfig, FileWriter writer,
                          Map<ProcessorTypeEnum, List<RdfFileProcessorSpi>> processors) {
        FileMeta fileMeta = TemplateLoader.load(fileConfig);
        List<FileColumnMeta> columnMetas = fileMeta.getTailColumns();

        if (columnMetas.size() == 0) {
            throw new RuntimeException("模板中没有配置文件体，不支持此操作");
        }

        ProtocolDefinition pd = ProtocolLoader.loadProtocol(fileMeta.getProtocol());
        List<RowDefinition> rds = pd.getTails();

        if (rds.size() == 0) {
            throw new RuntimeException("协议中没有定义文件体");
        }

        RowsCodec.serialize(bean, fileConfig, writer, processors, FileDataTypeEnum.TAIL);
    }

    /** 
     * @see hongwei.quhw.file.codec.FileCodec#deserialize(hongwei.quhw.file.common.ProtocolFileReader)
     */
    @Override
    public <T> T deserialize(Class<?> clazz, FileConfig fileConfig, FileReader reader,
                             Map<ProcessorTypeEnum, List<RdfFileProcessorSpi>> processors) {
        FileMeta fileMeta = TemplateLoader.load(fileConfig);
        List<FileColumnMeta> columnMetas = fileMeta.getTailColumns();

        if (columnMetas.size() == 0) {
            throw new RuntimeException("模板中没有配置文件体，不支持此操作");
        }

        ProtocolDefinition pd = ProtocolLoader.loadProtocol(fileMeta.getProtocol());
        List<RowDefinition> rds = pd.getTails();

        if (rds.size() == 0) {
            throw new RuntimeException("协议中没有定义文件体");
        }

        return RowsCodec.deserialize(clazz, fileConfig, reader, processors, FileDataTypeEnum.TAIL);
    }

}
