package edu.fsu.cs.contextprovider.rpc;

interface IContextProviderService {
	Map getAll();
	double proximityToAddress(String loc);
	double proximityToExact(double longitude, double latitude);
	double[] getCurrentCoordinates();
	String getCurrentAddress();
}