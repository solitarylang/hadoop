/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hadoop.mapreduce;

import java.io.IOException;

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.classification.InterfaceStability.Evolving;
import org.apache.hadoop.mapred.SplitLocationInfo;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.RecordReader;

/**
 * InputSplit代表的就是一个独立mapper处理的数据，它是面向字节的(即是一系列字节的集合)
 * 也是记录读取器处理的任务
 */
@InterfaceAudience.Public
@InterfaceStability.Stable
public abstract class InputSplit {
  /**
   * 获得切片的大小以便于按照大小进行排序(这里涉及到后续combineInputFormat的实现)
   */
  public abstract long getLength() throws IOException, InterruptedException;

  /**
   * 获得切片数据存储的节点列表(即物理存储的节点列表)
   */
  public abstract 
    String[] getLocations() throws IOException, InterruptedException;
  
  /**
   * 获得切片存储在哪些节点上以及在各个位置如何存储的
   * 返回null表示所有位置的数据都是存在磁盘上的
   */
  @Evolving
  public SplitLocationInfo[] getLocationInfo() throws IOException {
    return null;
  }
}