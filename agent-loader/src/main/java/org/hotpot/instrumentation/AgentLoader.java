/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package org.hotpot.instrumentation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AgentLoader {
    private static Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) {
        //iterate all jvms and get the first one that matches our application name
        List<VirtualMachineDescriptor> vmList = VirtualMachine.list();
        int vmCount = vmList.size();

        for (int i = 0; i < vmCount; i++) {
            VirtualMachineDescriptor vm = vmList.get(i);
            String vmDisplayName = vm.displayName();
            String pid = vm.id();
            System.out.println("["+ i + "] " + vmDisplayName + " pid: " + pid);
        }
        
        System.out.print("Choose JVM [0 - " + (--vmCount) + "]: ");
        String vmNumber = System.console().readLine();
        VirtualMachineDescriptor selectedVM = vmList.get(Integer.parseInt(vmNumber));
        System.out.println("You choose JVM " + selectedVM.displayName() + " pid: " + selectedVM.id());

        try {
            String agentJarPathStr;
            try {
                Path agentJarPath = Paths.get(HotPotAgent.class.getProtectionDomain().getCodeSource().getLocation().toURI());
                agentJarPathStr = agentJarPath.toAbsolutePath().toString();
                LOGGER.info("Using agent jar [{}].", agentJarPathStr);
            } catch (URISyntaxException e) {
                LOGGER.error("Agent jar not found.", e);
                return;
            }

            String jvmPid = selectedVM.id();
            LOGGER.info("Attaching to target JVM with PID: " + jvmPid);

            String configFilePath = null;
            if(args.length == 1) {
                configFilePath = args[0];
            }

            VirtualMachine vm = VirtualMachine.attach(jvmPid);
            vm.loadAgent(agentJarPathStr, configFilePath);
            vm.detach();
            LOGGER.info("Attached to target JVM and loaded HotPot agent successfully.");
        } catch (AttachNotSupportedException e) {
            LOGGER.info("Target JVM did not support attach agent.", e);
        } catch (IOException e) {
            LOGGER.info("Attached to target JVM and load HotPot agent failed.", e);
        } catch (AgentLoadException e) {
            LOGGER.info("Could not load HotPot agent.", e);
        } catch (AgentInitializationException e) {
            LOGGER.error("Init Error", e);
            LOGGER.error("Return Code {}", e.returnValue());
        }
    }
}
