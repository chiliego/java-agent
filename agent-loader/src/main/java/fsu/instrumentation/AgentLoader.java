/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package fsu.instrumentation;

import java.io.IOException;

import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

public class AgentLoader {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        try {
            VirtualMachine vm = VirtualMachine.attach(args[0]);
            //jvm.loadAgent(agentFile.getAbsolutePath());
            //jvm.detach();
        } catch (AttachNotSupportedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(new AgentLoader().getGreeting());
    }
}
