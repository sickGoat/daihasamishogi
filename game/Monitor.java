package game;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import board.implementation.Move;
import board.implementation.Player;

public class Monitor implements MonitorBroker, MonitorConnector {
	
	private Lock lock = new ReentrantLock();
	
	private Condition brokerBed = lock.newCondition();
	
	private Move move;
	
	private Player player;
	
	private boolean timeOver;
	
	private boolean moveSetted;
	
	private boolean myTurn;
	
	
	@Override
	public void setOpponentMove(Move move) {
		lock.lock();
		try{
			this.move = move;
			this.moveSetted = true;
			brokerBed.signalAll();
		}finally{
			lock.unlock();
		}
	}

	@Override
	public void yourTurn() {
		lock.lock();
		try{
			myTurn = true;
			timeOver = false;
			brokerBed.signalAll();
		}finally{
			lock.unlock();
		}
	}

	@Override
	public Move getMove() {
		lock.lock();
		Move res = null;
		try{
			this.timeOver = true;
			this.myTurn = false;
			res = this.move;
			this.move = null;
		}finally{
			lock.unlock();
		}
		return res;
	}

	@Override
	public void setPLayer(Player player) {
		lock.lock();
		try{
			this.player = player;
			brokerBed.signalAll();
		}finally{
			lock.unlock();
		}
	}

	@Override
	public void setTimeOver() {
		lock.lock();
		try{
			this.timeOver = true;
		}finally{
			lock.unlock();
		}
	}

	@Override
	public Move getOpponentMove() {
		lock.lock();
		Move res = null;
		try{
			if(!moveSetted){
				brokerBed.await();
			}
			moveSetted = false;
			res = this.move;
		}catch (Exception e) {
		}finally{
			lock.unlock();
		}
		return res;
	}

	@Override
	public void myTurn() {
		lock.lock();
		try{
			if(!myTurn){
				brokerBed.await();
			}
		}catch(InterruptedException e){	
		}finally{
			lock.unlock();
		}
	}

	@Override
	public void setMove(Move move) {
		lock.lock();
		try{
			if(!timeOver)
			this.move = move;
		}finally{
			lock.unlock();
		}
	}

	@Override
	public Player getPLayer() {
		lock.lock();
		Player player = null;
		try{
			if( player == null ){
				brokerBed.await();
			}
			player = this.player;
		}catch(InterruptedException e){		
		}finally{
			lock.unlock();
		}
		return player;
	}

	@Override
	public boolean isTimeOver() {
		lock.lock();
		boolean timeOver = false;
		try{
			timeOver = this.timeOver;
		}finally{
			lock.unlock();
		}
		return timeOver;
	}

}
