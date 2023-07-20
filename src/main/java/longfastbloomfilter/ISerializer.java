package longfastbloomfilter;

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

import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

//FiXME: ver o que se pode alterar com a licença
/**
 * Allows to serialize or deserialize the given type.
 * @param <T>   the given type.
 */
public interface ISerializer<T> {

    /**
     * Serialize the specified type into the specified DataOutputStream instance.
     * @param t         type that will be serialized
     * @param dataOutS  data into which serialization needs to happen
     * @throws IOException
     */
    void serialize(T t, DataOutputStream dataOutS) throws IOException;

    /**
     * Deserialize into the specified DataInputStream instance.
     * @param dataInS   data into which deserialization needs to happen
     * @return          the type that was deserialized
     * @throws IOException
     */
    T deserialize(DataInputStream dataInS) throws IOException;
}
