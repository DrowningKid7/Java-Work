import java.io.*;
import java.net.SocketException;

public class TXTFunction {
    public static void main(String[] args) {
        try { // ��ֹ�ļ��������ȡʧ�ܣ���catch��׽���󲢴�ӡ��Ҳ����throw

            /* ����TXT�ļ� */
            String pathname = "ccc.txt"; // ����·�������·�������ԣ������Ǿ���·����д���ļ�ʱ��ʾ���·��
            File filename = new File(pathname); // Ҫ��ȡ����·����input��txt�ļ�
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename),"GBK"); // ����һ������������reader
            BufferedReader br = new BufferedReader(reader); // ����һ�����������ļ�����ת�ɼ�����ܶ���������
            String line = "";
            line = br.readLine();
            while (line != null) {
                line = br.readLine(); // һ�ζ���һ������
                System.out.println(line);
            }

            /* д��Txt�ļ� */
            File writename = new File("output.txt"); // ���·�������û����Ҫ����һ���µ�output��txt�ļ�
            writename.createNewFile(); // �������ļ�
            OutputStreamWriter Writer = new OutputStreamWriter(
                    new FileOutputStream(writename),"GBK");
            BufferedWriter out = new BufferedWriter(Writer);;
            out.write("�һ�д���ļ���!!!\r\n"); // \r\n��Ϊ����
            out.newLine();
            out.write("�һ�д���ļ���!!!\r\n"); // \r\n��Ϊ����
            out.newLine();
            out.write("�һ�д���ļ���!!!\r\n"); // \r\n��Ϊ����
            out.newLine();
            out.flush(); // �ѻ���������ѹ���ļ�
            out.close(); // ���ǵùر��ļ�

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
