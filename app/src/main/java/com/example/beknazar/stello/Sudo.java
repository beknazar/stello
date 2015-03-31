package com.example.beknazar.stello;

/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/**
 * This program will demonstrate how to exec 'sudo' on the remote.
 *
 */
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.InputStream;
import java.io.OutputStream;

public class Sudo {

    public String ledOn(String host) {
        try{
            JSch jsch=new JSch();
//            Hashtable<String, String> ht = new Hashtable<>();
//            ht.put()

//            String host="192.168.0.2";
            String user="pi";


            Session session=jsch.getSession(user, host, 22);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            session.setPassword("software");
            session.connect();


            String command= "./ledon";

            String sudo_pass="software";

            Channel channel=session.openChannel("exec");

            // man sudo
            //   -S  The -S (stdin) option causes sudo to read the password from the
            //       standard input instead of the terminal device.
            //   -p  The -p (prompt) option allows you to override the default
            //       password prompt and use a custom one.
            ((ChannelExec)channel).setCommand("sudo " + command);


            InputStream in=channel.getInputStream();
            OutputStream out=channel.getOutputStream();
            ((ChannelExec)channel).setErrStream(System.err);

            channel.connect();

            out.write((sudo_pass+"\n").getBytes());
            out.flush();

            byte[] tmp=new byte[1024];
            while(true){
                while(in.available()>0){
                    int i=in.read(tmp, 0, 1024);
                    if(i<0)break;
                    System.out.print(new String(tmp, 0, i));
                }
                if(channel.isClosed()){
                    System.out.println("exit-status: "+channel.getExitStatus());
                    break;
                }
                try{Thread.sleep(1000);}catch(Exception ee){}
            }
            channel.disconnect();
            session.disconnect();
        }
        catch(Exception e){
            return e.toString();
        }
        return "OK!";
    }

    public String ledOff(String host) {
        try{
            JSch jsch=new JSch();

//            String host="192.168.0.2";
            String user="pi";


            Session session=jsch.getSession(user, host, 22);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            session.setPassword("software");
            session.connect();


            String command= "./ledoff";

            String sudo_pass="software";

            Channel channel=session.openChannel("exec");

            // man sudo
            //   -S  The -S (stdin) option causes sudo to read the password from the
            //       standard input instead of the terminal device.
            //   -p  The -p (prompt) option allows you to override the default
            //       password prompt and use a custom one.
            ((ChannelExec)channel).setCommand("sudo " + command);


            InputStream in=channel.getInputStream();
            OutputStream out=channel.getOutputStream();
            ((ChannelExec)channel).setErrStream(System.err);

            channel.connect();

            out.write((sudo_pass+"\n").getBytes());
            out.flush();

            byte[] tmp=new byte[1024];
            while(true){
                while(in.available()>0){
                    int i=in.read(tmp, 0, 1024);
                    if(i<0)break;
                    System.out.print(new String(tmp, 0, i));
                }
                if(channel.isClosed()){
                    System.out.println("exit-status: "+channel.getExitStatus());
                    break;
                }
                try{Thread.sleep(1000);}catch(Exception ee){}
            }
            channel.disconnect();
            session.disconnect();
        }
        catch(Exception e){
            return e.toString();
        }
        return "OK!";
    }


}

