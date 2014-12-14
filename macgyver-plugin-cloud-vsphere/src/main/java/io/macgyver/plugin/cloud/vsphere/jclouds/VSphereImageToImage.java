/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.macgyver.plugin.cloud.vsphere.jclouds;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.inject.Singleton;

import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.ImageBuilder;
import org.jclouds.compute.domain.OperatingSystem;
import org.jclouds.compute.domain.OsFamily;
import org.jclouds.compute.domain.Image.Status;
import org.jclouds.compute.reference.ComputeServiceConstants;
import org.jclouds.logging.Logger;

import com.google.common.base.Function;

@Singleton
public class VSphereImageToImage implements Function<io.macgyver.plugin.cloud.vsphere.jclouds.Image, Image> {
   @Resource
   @Named(ComputeServiceConstants.COMPUTE_LOGGER)
   protected Logger logger = Logger.NULL;

   @Override
   public Image apply(io.macgyver.plugin.cloud.vsphere.jclouds.Image from) {

      ImageBuilder builder = new ImageBuilder();
      builder.ids(from.id + "");
      builder.name(from.name);
      builder.description(from.name);

      OsFamily family = null;
      try {
         family = OsFamily.fromValue(from.name);
         builder.operatingSystem(new OperatingSystem.Builder().name(from.name).family(family).build());
      } catch (IllegalArgumentException e) {
         logger.debug("<< didn't match os(%s)", from);
      }
      builder.status(Status.AVAILABLE);
      return builder.build();
   }

}