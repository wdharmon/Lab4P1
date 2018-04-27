package app;

import java.io.IOException;

import app.controller.BlackJackController;
import app.controller.ClientServerStartController;
import app.controller.GameBorderController;
import app.hub.GameHub;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import netgame.common.Client;
import pkgCore.Action;
import pkgCore.GamePlay;
import pkgCore.Player;
import pkgCore.Table;
import pkgEnum.eAction;

public class Flamingo extends Application {

	private Stage primaryStage;
	private GameHub gHub = null;
	private BorderPane GameBorderPane = null;
	private BorderPane BlackJackBorderPane = null;
	private GameBorderController GBC = null;
	private BlackJackController BJC;
	private GameClient gClient = null;
	private Player appPlayer;

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		showClientServer(primaryStage);

	}

	public void setPlayer(Player player) {
		this.appPlayer = player;
	}

	public Player getAppPlayer() {
		return appPlayer;
	}

	public void ShowBlackJack(boolean bStartHub, String strComputerName, int iPort, String strPlayerName) {
		SetHubClient(bStartHub, strComputerName, iPort, strPlayerName);

		try {

			FXMLLoader loader = new FXMLLoader();
			loader = new FXMLLoader(getClass().getResource("/game/app/view/BlackJack.fxml"));
			BlackJackBorderPane = (BorderPane) loader.load();
			Scene scene = new Scene(BlackJackBorderPane);
			primaryStage.setScene(scene);
			BJC = loader.getController();

			BJC.setMainApp(this);
			primaryStage.show();

			/*
			Action act = new Action();
			act.setAction(eAction.TableState);
			act.setPlayer(this.getAppPlayer());
			this.SendMessageToClient(act);
			*/

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void SetHubClient(boolean bStartHub, String strComputerName, int iPort, String strPlayerName) {

		if (bStartHub) {
			try {
				gHub = new GameHub(iPort);

			} catch (Exception e) {
				System.out.println("Error: Can't listen on port " + iPort);
				return;
			}
		}
		try {
			gClient = new GameClient(strComputerName, iPort);
		} catch (IOException e) {
			e.printStackTrace();
		}

		setPlayer(new Player(strPlayerName, gClient.getID()));

		// HandleRoot();

		// ShowBlackJack();

	}

	public void showClientServer(Stage primaryStage) {
		try {
			// Load person overview.
			FXMLLoader loader = new FXMLLoader();

			loader = new FXMLLoader(getClass().getResource("/game/app/view/ClientServerStart.fxml"));

			BorderPane ClientServerOverview = (BorderPane) loader.load();

			Scene scene = new Scene(ClientServerOverview);

			primaryStage.setScene(scene);

			// Give the controller access to the main app.
			ClientServerStartController controller = loader.getController();
			controller.setMainApp(this);
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void HandleRoot() {
		// Parent root;
		try {

			FXMLLoader loader = new FXMLLoader();
			loader = new FXMLLoader(getClass().getResource("/game/app/view/GameBorder.fxml"));
			GameBorderPane = (BorderPane) loader.load();
			Scene scene = new Scene(GameBorderPane);
			primaryStage.setScene(scene);
			GBC = loader.getController();
			GBC.setMainApp(this);
			primaryStage.show();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void SendMessageToClient(Object Message) {
		gClient.messageSend(Message);
	}

	private class GameClient extends Client {

		public GameClient(String hubHostName, int hubPort) throws IOException {
			super(hubHostName, hubPort);
		}

		/*
		 * messageSend - One single place to send messages
		 */
		protected void messageSend(Object message) {
			// System.out.println("Sending message from MainApp.Client");
			resetOutput();
			super.send(message);
		}

		/*
		 * messageReceived will get an Object message... it's up to you to determine
		 * what should happen to that the message.
		 * 
		 * If it's a Table, handle Table - level action If it's a GamePlay, handle
		 * GamePlay - level action
		 */
		@Override
		protected void messageReceived(final Object message) {
			Platform.runLater(() -> {
				// System.out.println("Message Received. The message: " + message);

				if (message instanceof String) {
					System.out.println("Message Received " + message);
				} else if (message instanceof Table) {
					BJC.HandleTableState((Table) message);
				} else if (message instanceof GamePlay) {

				}

			});
		}

		@Override
		/*
		 * serverShutdown - Call the hard exit.
		 */
		protected void serverShutdown(String message) {

			Platform.runLater(() -> {
				Platform.exit();
				System.exit(0);
			});
		}

	}

}
