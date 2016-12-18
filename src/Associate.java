import java.io.*;
import java.util.*;

public class Associate {
	private static ArrayList[] list = new ArrayList [26];
	public Associate(){
		for(int i=0;i<26;i++)
			list[i] = new ArrayList();
	}
	
	public void readfile() throws FileNotFoundException{
		FileInputStream file = new FileInputStream("dictionary3.txt");
		Scanner input  = new Scanner(file);
		while(input.hasNext()){
			//count++;
			String s = input.nextLine();
			String[] temp = s.split("\t");
			//System.out.print(s + '\t');
			int x = s.toLowerCase().charAt(0) - 'a';
			//System.out.println(x);
			list[x].add(temp[0]);
			//list2[x].add(temp[1]);
		}
		input.close();
	}
	
	private static int min(int one, int two, int three) { 
        int min = one; 
        if(two < min) { 
            min = two; 
        } 
        if(three < min) { 
            min = three; 
        } 
        return min; 
    } 

	
	public int ld(String str1, String str2) { 
        int d[][];    //矩阵 
        int n = str1.length(); 
        int m = str2.length(); 
        int i;    //遍历str1的 
        int j;    //遍历str2的 
        char ch1;    //str1的 
        char ch2;    //str2的 
        int temp;    //记录相同字符,在某个矩阵位置值的增量,不是0就是1 
        if(n == 0) { 
            return m; 
        } 
        if(m == 0) { 
            return n; 
        } 
        d = new int[n+1][m+1]; 
        for(i=0; i<=n; i++) {    //初始化第一列 
            d[i][0] = i; 
        } 
        for(j=0; j<=m; j++) {    //初始化第一行 
            d[0][j] = j; 
        } 
        for(i=1; i<=n; i++) {    //遍历str1 
            ch1 = str1.charAt(i-1); 
            //去匹配str2 
            for(j=1; j<=m; j++) { 
                ch2 = str2.charAt(j-1); 
                if(ch1 == ch2) { 
                    temp = 0; 
                } else { 
                    temp = 1; 
                } 
                //左边+1,上边+1, 左上角+temp取最小 
                d[i][j] = min(d[i-1][j]+1, d[i][j-1]+1, d[i-1][j-1]+temp); 
            } 
        } 
        return d[n][m]; 
    } 
	
	public String[] ErrorCorrection(String s){
		//int x = s.toLowerCase().charAt(0) - 'a';
		String t1=s.toLowerCase();
		String t3="";
		for(int i=0;i<t1.length()-1;i++){
			if((int)t1.charAt(i)==32&&(int)t1.charAt(i+1)==32)
				continue;
			t3 += t1.charAt(i);
		}
		if((int)t1.charAt(t1.length()-1)!=32)
			t3 += t1.charAt(t1.length()-1);
		String[] result=new String[20];
		//int[] dis=new int[list[x].size()];
		int[] dis=new int[40000];
		int count=0;
		//reset();
		int num=0;
		for(int i=0;i<26;i++)
		{
			for(int j = 0 ; j<list[i].size();j++){
				String t2 = list[i].get(j).toString();
				dis[num]=ld(t3,t2);
				num++;
			}
		}
		/*for(int i=0;i<list[x].size();i++)
		{
			if(dis[i]==0)
			{
				result[count]=list[x].get(i).toString();
				return result;
			}
		}*/
		num=0;
		//int num=0;
		for(int i=0;i<26;i++)
		{
			for(int j=0;j<list[i].size();j++){
				if(dis[num]==1&&count<20){
					result[count]=list[i].get(j).toString();
					count++;
					//num++;
				}
				if(count>=20)
					return result;
				num++;
					//break;
			}
		}
		num=0;
		for(int i=0;i<26;i++)
		{
			for(int j=0;j<list[i].size();j++){
				if(dis[num]==2&&count<20){
					result[count]=list[i].get(j).toString();
					count++;
					//num++;
				}
				if(count>=20)
					return result;
				num++;
					//break;
			}
		}
		return result;
	}
	
	public String[] FindSimilar(String s){
		int x = s.toLowerCase().charAt(0) - 'a';
		String t1=s.toLowerCase();
		String t3="";
		for(int i=0;i<t1.length()-1;i++){
			if((int)t1.charAt(i)==32&&(int)t1.charAt(i+1)==32)
				continue;
			t3 += t1.charAt(i);
		}
		if((int)t1.charAt(t1.length()-1)!=32)
			t3 += t1.charAt(t1.length()-1);
		//reset();
		//System.out.println(t3);
		String[] result = new String[20];
		int count=0;
		for(int i = 0 ; i<list[x].size();i++){
			String t = list[x].get(i).toString();
			if(t.length() >= t3.length()){
				String t2 = t.substring(0, t3.length());
				if(t2.equals(t3)&&count<20){
					result[count]=t;
					count++;
				}
				if(count>=20)
					break;
					//result.add(t);
			}
		}
		return result;
	}
}
