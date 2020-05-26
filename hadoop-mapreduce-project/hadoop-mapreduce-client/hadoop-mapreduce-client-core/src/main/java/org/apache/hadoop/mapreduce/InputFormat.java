/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hadoop.mapreduce;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

/**
 * InputFormat描述的map-reduce任务的输入情况
 *  1.验证作业输入的规范性
 *  2.将输入文件划分成多个切片，每一个切片对应单独的mapper任务
 *  3.提供记录读取器从mapper的逻辑分片中获取输入记录
 *
 *  FileInputFormat就是根据输入文件的大小进行逻辑切片划分的
 *  但是，文件系统中文件的块大小是切片的上限，切片大小的下限可以通过
 *  mapreduce.input.fileinputformat.split.minsize参数进行指定
 *
 *  很明显，基于输入文件大小进行的逻辑切片划分不太能满足很多应用，因为记录界限也需要满足
 *  这种情况下，应用就不得不实现满足记录上下界的面向条数的记录读取器。
 *
 */
@InterfaceAudience.Public
@InterfaceStability.Stable
public abstract class InputFormat<K, V> {

    /**
     * 数据源文件的逻辑切片而不是物理切成多块，即这里是一个文件如何划分的规则
     * 每一片文件应该是<>文件路径，起点，片步长</>的元组形式
     * 比如一个200m文件，输入切片成<>/user/log/application.log,0,128*1024*1024</>和
     * <>/user/log/application.log,128*1024*1024,72*1024*1024</>两片
     * 实际上文件还是存储在一起的，只是通过索引分开了
     */
    public abstract List<InputSplit> getSplits(JobContext context
    ) throws IOException, InterruptedException;

    /**
     * 为给定的切片创建一个记录阅读器，按照规则读取每一个切片的内容给每一个mapper
     * 在切片被使用之前先调用RecordReader.initialize(InputSplit, TaskAttemptContext)方法
     */
    public abstract RecordReader<K, V> createRecordReader(InputSplit split,
                                                          TaskAttemptContext context
    ) throws IOException,
            InterruptedException;

}

