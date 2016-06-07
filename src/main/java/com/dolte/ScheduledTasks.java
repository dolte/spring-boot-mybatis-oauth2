package com.dolte;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 스케줄러
 * 
 * <pre>
 * Spring Task scheduler를 이용하여 배치작업 수행
 * </pre
 * @author adnstyle
 *
 */
@Component
public class ScheduledTasks {
	 
	private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
	
	/**
	 * 매일 0시
	 * </pre>
	 */
	@Scheduled(cron = "0 0 0 * * *")
	public void deleteSecedeUser() {
	}
	
	/**
	 * 10분 단위
	 * @throws Exception
	 */
	@Scheduled(cron = "0 0/10 * * * *")
	public void interfaceTest() throws Exception {
	}
}
