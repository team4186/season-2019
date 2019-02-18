package frc.robot;

public class ArcadeMode {
	public Result drive(double speed, double rotate) {	
		final double a = speed*speed;
		final double b = rotate*rotate;
		final double moveSig = Math.copySign(1, speed);
		final double sigSub = (a - b)*moveSig;
		final double sigMax = Math.max(a, b)*moveSig;
				
		if(moveSig == Math.copySign(1, rotate)) {
			return new Result(-sigSub, -sigMax);	
		}
		else {	
			return new Result(-sigMax, -sigSub);	
		}
	}
	
	public static class Result {	
		
		public Result(double leftSpeed, double rightSpeed) {	
			this.leftSpeed = leftSpeed;
			this.rightSpeed = rightSpeed;	
		}
		
		public final double leftSpeed;
		public final double rightSpeed;
	}
}
