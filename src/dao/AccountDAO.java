public boolean transferFunds(String fromAccNo, String toAccNo, BigDecimal amount) {
    Connection conn = null;
    try {
        conn = DBUtil.getConnection();
        conn.setAutoCommit(false);

        // SELECT FOR UPDATE to lock rows
        PreparedStatement ps1 = conn.prepareStatement(
            "SELECT id, balance FROM accounts WHERE account_number=? FOR UPDATE");
        ps1.setString(1, fromAccNo);
        ResultSet rs1 = ps1.executeQuery();
        if (!rs1.next()) throw new SQLException("From account not found");
        long fromId = rs1.getLong("id");
        BigDecimal fromBal = rs1.getBigDecimal("balance");

        ps1.setString(1, toAccNo);
        PreparedStatement ps2 = conn.prepareStatement(
            "SELECT id, balance FROM accounts WHERE account_number=? FOR UPDATE");
        ps2.setString(1, toAccNo);
        ResultSet rs2 = ps2.executeQuery();
        if (!rs2.next()) throw new SQLException("To account not found");
        long toId = rs2.getLong("id");
        BigDecimal toBal = rs2.getBigDecimal("balance");

        if (fromBal.compareTo(amount) < 0) throw new SQLException("Insufficient funds");

        PreparedStatement upd = conn.prepareStatement("UPDATE accounts SET balance=? WHERE id=?");
        upd.setBigDecimal(1, fromBal.subtract(amount));
        upd.setLong(2, fromId);
        upd.executeUpdate();

        upd.setBigDecimal(1, toBal.add(amount));
        upd.setLong(2, toId);
        upd.executeUpdate();

        PreparedStatement ins = conn.prepareStatement(
            "INSERT INTO transactions (account_id, type, amount, description, related_account) VALUES (?, ?, ?, ?, ?)");
        ins.setLong(1, fromId);
        ins.setString(2, "DEBIT");
        ins.setBigDecimal(3, amount);
        ins.setString(4, "Transfer to " + toAccNo);
        ins.setString(5, toAccNo);
        ins.executeUpdate();

        ins.setLong(1, toId);
        ins.setString(2, "CREDIT");
        ins.setBigDecimal(3, amount);
        ins.setString(4, "Transfer from " + fromAccNo);
        ins.setString(5, fromAccNo);
        ins.executeUpdate();

        conn.commit();
        return true;
    } catch (Exception e) {
        if (conn != null) try { conn.rollback(); } catch (SQLException ex) {}
        e.printStackTrace();
        return false;
    } finally {
        if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ex) {}
    }
}
