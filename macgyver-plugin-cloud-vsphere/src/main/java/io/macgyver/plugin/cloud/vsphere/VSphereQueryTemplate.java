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
package io.macgyver.plugin.cloud.vsphere;

import static io.macgyver.plugin.cloud.vsphere.ManagedObjectTypes.VIRTUAL_MACHINE;

import java.rmi.RemoteException;
import java.util.List;

import com.google.common.collect.Lists;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;

public class VSphereQueryTemplate {


	private ServiceInstance serviceInstance;

	public VSphereQueryTemplate(ServiceInstance serviceInstance) {
		this.serviceInstance = serviceInstance;
	}

	ServiceInstance getServiceInstance() {
		return serviceInstance;
	}

	
	public Iterable<HostSystem> findAllHostSystems() {
		try {

			InventoryNavigator nav = new InventoryNavigator(
					getServiceInstance().getRootFolder());

			ManagedEntity[] entitites = nav
					.searchManagedEntities(ManagedObjectTypes.HOST_SYSTEM);
			List<HostSystem> vmList = Lists.newArrayList();

			for (ManagedEntity me : entitites) {
				vmList.add((HostSystem) me);
			}

			return java.util.Collections.unmodifiableList(vmList);
		} catch (RemoteException e) {
			throw new VSphereExceptionWrapper(e);
		}

	}
	
	public Iterable<VirtualMachine> findAllVirtualMachines() {
		try {

			InventoryNavigator nav = new InventoryNavigator(
					getServiceInstance().getRootFolder());

			ManagedEntity[] entitites = nav
					.searchManagedEntities(VIRTUAL_MACHINE);
			List<VirtualMachine> vmList = Lists.newArrayList();

			for (ManagedEntity me : entitites) {
				vmList.add((VirtualMachine) me);
			}

			return java.util.Collections.unmodifiableList(vmList);
		} catch (RemoteException e) {
			throw new VSphereExceptionWrapper(e);
		}

	}

}
