
public class Test2 {
	 public void changeNum() {
         Test1.num = 100;
      }

      public static void main(String[] args) {
             Test2 t = new Test2();
              t.changeNum();
             System.out.println("num=====" + Test1.num);
             try {
                   Thread.sleep(5000);
               } catch (InterruptedException e) {
                     e.printStackTrace();
              }
         }

}
