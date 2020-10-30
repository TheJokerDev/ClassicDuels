package me.despical.classicduels.user.data;

import me.despical.classicduels.api.StatsStorage;
import me.despical.classicduels.user.User;

/**
 * @author Despical
 * @since 1.0.0
 * <p>
 * Created at 02.07.2020
 */
public interface UserDatabase {

	/**
	 * Saves player statistic into yaml or MySQL storage based on user choice
	 *
	 * @param user user to retrieve statistic from
	 * @param stat stat to save to storage
	 */
	void saveStatistic(User user, StatsStorage.StatisticType stat);

	/**
	 * Saves player statistic into yaml or MySQL storage based on user choice
	 *
	 * @param user user to retrieve statistic from
	 */
	void saveAllStatistic(User user);

	/**
	 * Loads player statistic from yaml or MySQL storage based on user choice
	 *
	 * @param user user to load statistic for
	 */
	void loadStatistics(User user);

}