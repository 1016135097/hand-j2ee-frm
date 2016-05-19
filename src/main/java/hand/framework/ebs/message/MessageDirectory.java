package hand.framework.ebs.message;

import hand.framework.ebs.util.EBSContext;
import oracle.apps.fnd.common.MessageToken;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Stack;

/**
 * MessageDirectory
 * EBS Message工具
 * @author Pany
 *
 */
public class MessageDirectory {

	private DataSource dataSource;

	private oracle.apps.fnd.ext.common.MessageDirectory messageDirectory;

	private oracle.apps.fnd.ext.common.MessageDirectory getMessageDirectory() {
		if (this.messageDirectory == null) {
			this.messageDirectory = new oracle.apps.fnd.ext.common.MessageDirectory();
		}
		return this.messageDirectory;
	}

	/**
	 * 获取消息文本
	 * @param appName 应用简称
	 * @param messageName 消息名称
	 * @return
	 */
	public String getMessageText(String appName, String messageName) {
		try {
			return getMessageDirectory().getMessageText(appName,
														messageName,
														dataSource.getConnection());
		} catch (SQLException e) {
			return null;
		}
	}

	/**
	 * 获取消息文本
	 * @param appName	应用简称
	 * @param messageName	消息名称
	 * @param tokens	参数
	 * @return
	 */
	public String getMessageText(String appName, String messageName,
			Stack<MessageToken> tokens) {
		try {
			EBSContext context = EBSContext.getInstance();

			return getMessageDirectory().getMessageText(appName,
														messageName,
														context.getLangCode(),
														tokens,
														dataSource.getConnection());
		} catch (SQLException e) {
			return null;
		}
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
