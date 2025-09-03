package com.mybank;

import java.util.Scanner;
import com.mybank.dao.CustomerDAO;
import com.mybank.dao.AccountDAO;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        CustomerDAO cdao = new CustomerDAO();
        AccountDAO adao = new AccountDAO();

        while(true) {
            System.out.println("1. Register Customer\n2. Open Account\n3. Transfer\n4. Exit");
            int ch = Integer.parseInt(sc.nextLine());
            if(ch == 1) {
                System.out.print("Name: "); String name = sc.nextLine();
                System.out.print("Email: "); String email = sc.nextLine();
                long id = cdao.createCustomer(name, email, null);
                System.out.println("Customer created id=" + id);
            } else if(ch == 2) {
                System.out.print("Customer id: "); long cid = Long.parseLong(sc.nextLine());
                System.out.print("Account number: "); String accNo = sc.nextLine();
                adao.openAccount(cid, accNo, new BigDecimal("0.00"), "SAVINGS");
                System.out.println("Account opened");
            } else if(ch == 3) {
                System.out.print("From account: "); String from = sc.nextLine();
                System.out.print("To account: "); String to = sc.nextLine();
                System.out.print("Amount: "); BigDecimal amt = new BigDecimal(sc.nextLine());
                boolean ok = adao.transferFunds(from, to, amt);
                System.out.println(ok ? "Transfer success" : "Transfer failed");
            } else break;
        }
        sc.close();
    }
}

