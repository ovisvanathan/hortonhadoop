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

#ifndef LIBHDFSPP_TOOLS_HDFS_RM_MOCK
#define LIBHDFSPP_TOOLS_HDFS_RM_MOCK

#include <functional>
#include <memory>
#include <string>
#include <vector>

#include <gmock/gmock.h>

#include "hdfs-rm.h"

namespace hdfs::tools::test {
/**
 * {@class RmMock} is an {@class Rm} whereby it mocks the
 * HandleHelp and HandlePath methods for testing their functionality.
 */
class RmMock : public hdfs::tools::Rm {
public:
  /**
   * {@inheritdoc}
   */
  RmMock(const int argc, char **argv) : Rm(argc, argv) {}

  // Abiding to the Rule of 5
  RmMock(const RmMock &) = delete;
  RmMock(RmMock &&) = delete;
  RmMock &operator=(const RmMock &) = delete;
  RmMock &operator=(RmMock &&) = delete;
  ~RmMock() override;

  /**
   * Defines the methods and the corresponding arguments that are expected
   * to be called on this instance of {@link HdfsTool} for the given test case.
   *
   * @param test_case An {@link std::function} object that points to the
   * function defining the test case
   * @param args The arguments that are passed to this test case
   */
  void SetExpectations(std::function<std::unique_ptr<RmMock>()> test_case,
                       const std::vector<std::string> &args = {}) const;

  MOCK_METHOD(bool, HandleHelp, (), (const, override));

  MOCK_METHOD(bool, HandlePath, (const bool, const std::string &),
              (const, override));
};
} // namespace hdfs::tools::test

#endif
