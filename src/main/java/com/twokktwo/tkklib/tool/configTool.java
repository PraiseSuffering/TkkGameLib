package com.twokktwo.tkklib.tool;

import com.twokktwo.tkklib.TkkGameLib;

import java.io.*;

public class configTool {
    public static boolean getConfigBool(String name){
        boolean returnValue=false;
        StringBuffer rt=new StringBuffer();
        try {
            File file = new File(TkkGameLib.MOD_DIR.getCanonicalPath() + "/main/");
            if (!file.exists()) {
                file.mkdirs();
            }
            File files = new File(file.getCanonicalPath() +"/ TkkLibConfig.config");
            if(!files.exists()) {
                files.createNewFile();
                FileWriter fw=new FileWriter(files);
                fw.write(getDefaultConfig());
                fw.close();
            }
            if (files.exists()) {
                FileReader fr = new FileReader(files);
                BufferedReader br = new BufferedReader(fr);
                String temp;
                while ((temp= br.readLine())!=null){
                    rt.append("\n"+temp);
                    String[] tempA=temp.split("=");
                    if(tempA.length==2){
                        if(tempA[0].equals(name)){
                            if (tempA[1].equals("True")){
                                returnValue=true;
                            }
                        }
                    }
                }
                fr.close();
                br.close();
            }
        } catch (FileNotFoundException e) {
            TkkGameLib.print("getJsFile() error:"+e);
        } catch (IOException e) {
            TkkGameLib.print("getJsFile() error:"+e);
        }
        return returnValue;
    }
    private static String getDefaultConfig(){
        StringBuffer sb=new StringBuffer();
        sb.append("Main Config");
        sb.append("\nFill in True or False");
        sb.append("\n");
        sb.append("\nWarning: Enabling this function will cause your computer to execute unknown code of the server you are connected to. Enabling this function means that you agree to assume the risk and the author of the module is not responsible.以上翻译结果来自有道神经网络翻译（YNMT）· 通用领域");
        sb.append("\n警告：开启本功能会让你的电脑执行你所链接的服务器的未知代码，开启本功能即代表你同意承担风险，模组作者不负任何责任。");
        sb.append("\ncanRunServerJs=False");
        return sb.toString();
    }
}
