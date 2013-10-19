package ebs.hb;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.hibernate.cfg.Configuration;

/**
 * Created by Aleksey Dubov.
 * Date: 10/25/11
 * Time: 2:09 AM
 * Copyright (c) 2011
 */
public interface DataSourceProvider {
	public ComboPooledDataSource configure(ComboPooledDataSource comboPooledDataSource);
	public Configuration configure(Configuration configuration);
}
