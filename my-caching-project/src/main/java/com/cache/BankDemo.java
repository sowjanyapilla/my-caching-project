package com.cache;
class BankAccount{
    private int balance=0;
    BankAccount(){
        balance=0;
    }
    public synchronized void deposit(int amount){
        balance+=amount;
        System.out.println("Deposited: "+amount+" New Balance: "+balance);
        notifyAll();
    }

    public synchronized void withdraw(int amount) {
        while (balance < amount) {
            try {
                System.out.println("Waiting for sufficient balance...");
                wait(); // Wait until sufficient balance is available
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        balance -= amount;
        System.out.println("Withdrawn: " + amount + " New Balance: " + balance);
    }

    // public synchronized void withdraw(int amount){
    //     if(balance>=amount){
    //         balance-=amount;
    //         System.out.println("Withdrawn: "+amount+" New Balance: "+balance);
    //     }else{
    //         System.out.println("Insufficient balance");
    //     }
    // }
    public int getBalance(){
        return balance;
    }
}
public class BankDemo {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Bank Demo");
        BankAccount account = new BankAccount();

    
        Thread depositThread = new Thread(() -> {
            for(int i=0;i<5;i++){
                account.deposit(100);
                try{
                    Thread.sleep(100);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        },"Deposit Thread");
        Thread withdrawThread = new Thread(() -> {
            for(int i=0;i<5;i++){
                account.withdraw(80);
                try{
                    Thread.sleep(100);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        },"Withdraw Thread");

        

        depositThread.start();
        withdrawThread.start();

    
        // try{
        //     depositThread.join();
        //     withdrawThread.join();
        // }catch(InterruptedException e){
        //     e.printStackTrace();
        // }
        // System.out.println("Final Balance: "+account.getBalance());
    }
}