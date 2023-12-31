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

package org.apache.hadoop.yarn.api.records.impl.pb;

import java.nio.channels.ClosedChannelException;

import org.junit.jupiter.api.Test;

import org.apache.hadoop.yarn.exceptions.YarnRuntimeException;
import org.apache.hadoop.yarn.proto.YarnProtos.SerializedExceptionProto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

public class TestSerializedExceptionPBImpl {
  @Test
  void testSerializedException() throws Exception {
    SerializedExceptionPBImpl orig = new SerializedExceptionPBImpl();
    orig.init(new Exception("test exception"));
    SerializedExceptionProto proto = orig.getProto();
    SerializedExceptionPBImpl deser = new SerializedExceptionPBImpl(proto);
    assertEquals(orig, deser);
    assertEquals(orig.getMessage(), deser.getMessage());
    assertEquals(orig.getRemoteTrace(), deser.getRemoteTrace());
    assertEquals(orig.getCause(), deser.getCause());
  }

  @Test
  void testDeserialize() throws Exception {
    Exception ex = new Exception("test exception");
    SerializedExceptionPBImpl pb = new SerializedExceptionPBImpl();

    try {
      pb.deSerialize();
      fail("deSerialize should throw YarnRuntimeException");
    } catch (YarnRuntimeException e) {
      assertEquals(ClassNotFoundException.class,
          e.getCause().getClass());
    }

    pb.init(ex);
    assertEquals(ex.toString(), pb.deSerialize().toString());
  }

  @Test
  void testDeserializeWithDefaultConstructor() {
    // Init SerializedException with an Exception with default constructor.
    ClosedChannelException ex = new ClosedChannelException();
    SerializedExceptionPBImpl pb = new SerializedExceptionPBImpl();
    pb.init(ex);
    assertEquals(ex.getClass(), pb.deSerialize().getClass());
  }

  @Test
  void testBeforeInit() throws Exception {
    SerializedExceptionProto defaultProto =
        SerializedExceptionProto.newBuilder().build();

    SerializedExceptionPBImpl pb1 = new SerializedExceptionPBImpl();
    assertNull(pb1.getCause());

    SerializedExceptionPBImpl pb2 = new SerializedExceptionPBImpl();
    assertEquals(defaultProto, pb2.getProto());

    SerializedExceptionPBImpl pb3 = new SerializedExceptionPBImpl();
    assertEquals(defaultProto.getTrace(), pb3.getRemoteTrace());
  }

  @Test
  void testThrowableDeserialization() {
    // java.lang.Error should also be serializable
    Error ex = new Error();
    SerializedExceptionPBImpl pb = new SerializedExceptionPBImpl();
    pb.init(ex);
    assertEquals(ex.getClass(), pb.deSerialize().getClass());
  }
}
